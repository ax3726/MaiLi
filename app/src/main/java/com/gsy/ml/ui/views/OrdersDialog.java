package com.gsy.ml.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RideStep;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.DialogOrdersBinding;
import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.home.PartTimeModel;
import com.gsy.ml.model.main.OrderInfoModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.model.message.OrderContent;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.PartTimePresenter;
import com.gsy.ml.prestener.home.SendOrdersPresenter;
import com.gsy.ml.ui.message.ChatActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.utils.LocationHelper;
import com.gsy.ml.ui.utils.LocationHelper.ILocationListener;
import com.gsy.ml.ui.utils.SpeedHelper;
import com.gsy.ml.ui.utils.ToastUtil;
import com.hyphenate.easeui.EaseConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.gsy.com.library.common.LoadingDialog;
import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/4/21.
 */

public class OrdersDialog extends Dialog implements ILocationListener, RouteSearch.OnRouteSearchListener, View.OnClickListener, ILoadPVListener {
    /**
     * 加载进度
     */
    private LoadingDialog mLoadingDialog;
    private Context mContext;
    private List<String> mDataList = new ArrayList<>();
    private LatLonPoint mStartPoint = null;//起点，39.942295,116.335891
    private LatLonPoint mEndPoint = null;//终点，39.995576,116.481288
    private RouteSearch mRouteSearch;
    private RideRouteResult mRideRouteResult;
    private final int ROUTE_TYPE_RIDE = 4;
    private SendOrdersPresenter mPresenter = new SendOrdersPresenter(this);
    private OrderInfoModel mOrderinfo = new OrderInfoModel();//订单信息
    private boolean isClick = true;//是否可以点击
    private PartTimePresenter presenter = new PartTimePresenter(this);

    public OrdersDialog(@NonNull Context context) {
        super(context, R.style.DialogBaseStyle);
        mContext = context;
    }

    public OrdersDialog(@NonNull Context context, OrderInfoModel info) {
        super(context, R.style.DialogBaseStyle);
        mContext = context;
        this.mOrderinfo = info;
    }

    private int mCurTime = 0;
    private int mMaxTime = 8;

    private Handler mTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mCurTime++;
            if (mCurTime < mMaxTime) {
                mBinding.tvCommitOrder.setText((8 - mCurTime) + "");
                mTimeHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTimeHandler.sendEmptyMessage(0);
                    }
                }, 1000);
            } else {
                mBinding.imgOrdersBg.setSelected(false);
                mBinding.tvCommitOrder.setText("抢单");
            }
        }
    };


    private String countDistance(String jing, String wei) {
        LatLng start = null;
        LatLng end = null;
        if (!TextUtils.isEmpty(jing) && !TextUtils.isEmpty(wei)) {
            start = new LatLng(Float.valueOf(wei), Float.valueOf(jing));

        } else {
            return "不详";
        }
        UserInfoModel.UserPlaceBean userPlace = MaiLiApplication.getInstance().getUserPlace();
        if (!TextUtils.isEmpty(userPlace.getWei()) && !TextUtils.isEmpty(userPlace.getJing())) {
            end = new LatLng(Float.valueOf(userPlace.getWei()), Float.valueOf(userPlace.getJing()));
            mEndPoint = new LatLonPoint(Float.valueOf(userPlace.getWei()), Float.valueOf(userPlace.getJing()));
        } else {
            return "不详";
        }

        int distance = (int) AMapUtils.calculateLineDistance(start, end);
        if (distance < 1000) {
            return distance + "m";
        } else {
            NumberFormat ddf1 = NumberFormat.getNumberInstance();//保留一位小数
            ddf1.setMaximumFractionDigits(1);
            float di = (float) distance / 1000;
            return ddf1.format(di) + "km";
        }
    }


    private String countSendDistance() {
        if (mOrderinfo == null) {
            return "";
        }
        OrderInfoModel.DataBean data = mOrderinfo.getData();
        String[] jing = data.getWorkJing().split(",");
        String[] wei = data.getWorkWei().split(",");
        LatLng start = new LatLng(Float.valueOf(data.getStartWei()), Float.valueOf(data.getStartJing()));
        int distance = 0;
        LatLng end = null;
        for (int i = 0; i < jing.length; i++) {
            if (i == 0) {
                end = new LatLng(Float.valueOf(wei[i]), Float.valueOf(jing[i]));
                distance = distance + (int) AMapUtils.calculateLineDistance(start, end);
            } else if (i < jing.length - 1) {
                LatLng end1 = new LatLng(Float.valueOf(wei[i]), Float.valueOf(jing[i]));
                end = new LatLng(Float.valueOf(wei[i + 1]), Float.valueOf(jing[i + 1]));
                distance = distance + (int) AMapUtils.calculateLineDistance(end1, end);
            } else {
                LatLng end1 = new LatLng(Float.valueOf(wei[i]), Float.valueOf(jing[i]));
                distance = distance + (int) AMapUtils.calculateLineDistance(end, end1);
            }
        }
        if (distance < 1000) {
            return distance + "m";
        } else {
            NumberFormat ddf1 = NumberFormat.getNumberInstance();//保留一位小数
            ddf1.setMaximumFractionDigits(1);
            float di = (float) distance / 1000;
            return ddf1.format(di) + "km";
        }

    }

    private DialogOrdersBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_orders, null, false);

        this.setContentView(mBinding.getRoot());
        setCancelable(false);
        initView();
        initOrder();
        if (Integer.valueOf(mOrderinfo.getData().getWorkType()) != 24) {//游戏陪练
            initLocation(savedInstanceState);
            initDrive();
        } else {
            mBinding.llyUp.setVisibility(View.GONE);
        }
        setListener();
        mCurTime = 0;
        mTimeHandler.sendEmptyMessage(0);//开始倒计时
        mBinding.imgOrdersBg.setSelected(true);
        mTimeHandler.postDelayed(timeRunnable, 60 * 1000);//30秒后自动关闭窗口
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mTimeHandler.removeCallbacks(timeRunnable);
            }
        });
    }


    @Override
    public void onDetachedFromWindow() {
        mTimeHandler.removeCallbacks(timeRunnable);
        super.onDetachedFromWindow();


    }

    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            if (mContext != null) {
                dismiss();
            }
        }
    };


    private void initOrder() {
        OrderInfoModel.DataBean data = mOrderinfo.getData();
        UserInfoModel.UserPlaceBean userPlace = MaiLiApplication.getInstance().getUserPlace();
        mStartPoint = new LatLonPoint(Float.valueOf(userPlace.getWei()), Float.valueOf(userPlace.getJing()));
        if (Integer.valueOf(data.getWorkType()) == 1) {
            mBinding.tvOrderInfo.setText("¥" + data.getWorkCost() + "/\t" + countSendDistance() + "\t/距你" + countDistance(data.getStartJing(), data.getStartWei()));
            String workWei = data.getWorkWei();
            String workJing = data.getWorkJing();
            String[] split_wei = workWei.split(",");
            String[] split_jing = workJing.split(",");
            if (split_wei != null && split_wei.length > 1) {
                mEndPoint = new LatLonPoint(Float.valueOf(split_wei[0]), Float.valueOf(split_jing[0]));
            } else {
                mEndPoint = new LatLonPoint(Float.valueOf(data.getWorkWei()), Float.valueOf(data.getWorkJing()));
            }


        } else {
            if (Integer.valueOf(data.getWorkType()) == 24) {//游戏陪练
                mBinding.tvOrderInfo.setText("¥" + data.getWorkCost());
            } else {
                mBinding.tvOrderInfo.setText("¥" + data.getWorkCost() + "/\t距你" + countDistance(data.getWorkJing(), data.getWorkWei()));
                mEndPoint = new LatLonPoint(Float.valueOf(data.getWorkWei()), Float.valueOf(data.getWorkJing()));
            }

        }
        mBinding.imgYuyue.setVisibility(data.getReservationTime() > 0 ? View.VISIBLE : View.GONE);

        String[] con = DemoUtils.TypeToContent(Integer.valueOf(data.getWorkType()), data.getWorkContent());
        String add_con = "";
        if (Integer.valueOf(data.getWorkType()) == 1) {
            add_con = "取件地址:" + data.getStartProvince()
                    + data.getStartCity()
                    + data.getStartArea()
                    + data.getStartPlace()
                    + data.getStartDoorplate();

            String workProvince = data.getWorkProvince();
            String workCity = data.getWorkCity();
            String[] area = data.getWorkArea().split(",");
            String[] place = data.getWorkPlace().split(",");
            String[] door = data.getWorkDoorplate().split(",");

            for (int i = 0; i < area.length; i++) {
                String num = area.length > 1 ? String.valueOf(i + 1) + ":" : ":";
                add_con = add_con + "<br/>" + "配送地址" + num + workProvince + workCity + area[i] + place[i] + door[i];
            }

        }
        mBinding.tvContent.setText(Html.fromHtml(con[0] + "<br/>" + con[1] + "<br/>" + add_con));

        mBinding.tvTitle.setText(DemoUtils.TypeToOccupation(Integer.valueOf(data.getWorkType())));

    }

    /**
     * 初始化控件
     */

    private void initView() {

        WindowManager m = ((Activity) mContext).getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = (int) ((d.getWidth()) * 0.9);
        params.height = params.height;
        this.getWindow().setAttributes(params);

        int w = ViewGroup.MeasureSpec.makeMeasureSpec(0,
                ViewGroup.MeasureSpec.UNSPECIFIED);
        int h = ViewGroup.MeasureSpec.makeMeasureSpec(0,
                ViewGroup.MeasureSpec.UNSPECIFIED);
        mBinding.svContent.measure(w, h);
        mBinding.llyAllBody.measure(w, h);
        mBinding.llyUp.measure(w, h);
        mBinding.rlyAddHead.measure(w, h);
        int height = mBinding.svContent.getMeasuredHeight();
        int head_height = mBinding.rlyAddHead.getMeasuredHeight();
        if (Utils.dip2px(mContext, height) >= d.getHeight() / 3 + head_height) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, d.getHeight() / 3 + head_height);
            mBinding.svContent.setLayoutParams(layoutParams);
        }
        mBinding.flyAllContent.measure(w, h);
        int all_height = mBinding.llyAllBody.getMeasuredHeight();
        int up_head_height = mBinding.llyUp.getMeasuredHeight();
        LinearLayout.LayoutParams all_layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, all_height);
        FrameLayout.LayoutParams map_layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, all_height - up_head_height);
        mBinding.flyAllContent.setLayoutParams(all_layoutParams);
        mBinding.mvLoaction.setLayoutParams(map_layoutParams);
        mBinding.svAllContent.setTitleHeight(up_head_height);
    }

    /**
     * 初始化监听
     */
    private void setListener() {
        mBinding.imgDel.setOnClickListener(this);

        mBinding.rlyCommitOrder.setOnClickListener(this);
        mBinding.svAllContent.setScrollViewListener(new OrdersScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(OrdersScrollView scrollView, int x, int y, int oldx, int oldy, boolean show_del) {
                if (show_del) {
                    mBinding.imgDel.setVisibility(View.VISIBLE);
                } else {
                    mBinding.imgDel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onUpPage() {
                mBinding.tvHint.setText("向上滑动查看地图");
                mBinding.imgUp.setVisibility(View.VISIBLE);
                mBinding.imgDown.setVisibility(View.GONE);
            }

            @Override
            public void onDownPage() {
                mBinding.tvHint.setText("向下滑动查看订单信息");
                mBinding.imgUp.setVisibility(View.GONE);
                mBinding.imgDown.setVisibility(View.VISIBLE);

            }
        });
    }

    //初始化地图控制器对象+
    AMap aMap;

    private void initLocation(Bundle savedInstanceState) {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mBinding.mvLoaction.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mBinding.mvLoaction.getMap();
        }


        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    mBinding.mvLoaction.getParent().requestDisallowInterceptTouchEvent(false);

                } else {
                    mBinding.mvLoaction.getParent().requestDisallowInterceptTouchEvent(true);
                }

            }
        });
        aMap.getUiSettings().setZoomControlsEnabled(false);
    }

    private void initDrive() {


        mRouteSearch = new RouteSearch(mContext);
        mRouteSearch.setRouteSearchListener(this);
        searchRouteResult(ROUTE_TYPE_RIDE, RouteSearch.DrivingDefault);
    }


    @Override
    public void onLocationChanged(AMapLocation location) {

        double lat = location.getLatitude();
        double lon = location.getLongitude();
        // setLocation(lat, lon);
    }

    private void setLocation(double lat, double lon) {
        // 设置当前地图显示为当前位置
        //  aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 17));
        // 设置当前地图显示为当前位置
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lon));
        markerOptions.title("当前位置");
        markerOptions.visible(true);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.location_mark_icon));
        markerOptions.icon(bitmapDescriptor);
        aMap.addMarker(markerOptions);

    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {

            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mRideRouteResult = result;
                    List<LatLng> points = new ArrayList<>();
                    for (RideStep d : result.getPaths().get(0).getSteps()) {
                        List<LatLonPoint> polyline = d.getPolyline();
                        for (LatLonPoint l : polyline) {
                            points.add(new LatLng(l.getLatitude(), l.getLongitude()));
                        }
                    }
                    addPolylineInPlayGround(points);

                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(mContext.getApplicationContext(), errorCode);
        }
    }


    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            ToastUtil.show(mContext, "定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            ToastUtil.show(mContext, "终点未设置");
        }

        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_RIDE) {// 骑行路径规划
            RouteSearch.RideRouteQuery query = new RouteSearch.RideRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateRideRouteAsyn(query);// 异步路径规划骑行模式查询
        }

       /* if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        }*/
    }

    /**
     * 移动镜头到当前的视角。
     *
     * @since V2.1.0
     */
    public void zoomToSpan(LatLng startPoint, LatLng endPoint) {
        if (startPoint != null) {
            if (aMap == null)
                return;
            try {
                LatLngBounds bounds = getLatLngBounds(startPoint, endPoint);
                aMap.animateCamera(CameraUpdateFactory
                        .newLatLngBounds(bounds, 100));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private LatLngBounds getLatLngBounds(LatLng startPoint, LatLng endPoint) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        b.include(new LatLng(startPoint.latitude, startPoint.longitude));
        b.include(new LatLng(endPoint.latitude, endPoint.longitude));

        return b.build();
    }

    private Polyline mPolyline;

    /**
     * 添加轨迹线
     */
    private void addPolylineInPlayGround(List<LatLng> list) {

        List<Integer> colorList = new ArrayList<Integer>();
        List<BitmapDescriptor> bitmapDescriptors = new ArrayList<BitmapDescriptor>();

        int[] colors = new int[]{Color.argb(255, 0, 255, 0), Color.argb(255, 255, 255, 0), Color.argb(255, 255, 0, 0)};

        //用一个数组来存放纹理
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        textureList.add(BitmapDescriptorFactory.fromResource(R.drawable.custtexture));

        List<Integer> texIndexList = new ArrayList<Integer>();
        texIndexList.add(0);//对应上面的第0个纹理
        texIndexList.add(1);
        texIndexList.add(2);

        Random random = new Random();
        for (int i = 0; i < list.size(); i++) {
            colorList.add(colors[random.nextInt(3)]);
            bitmapDescriptors.add(textureList.get(0));

        }

        mPolyline = aMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.custtexture)) //setCustomTextureList(bitmapDescriptors)
//				.setCustomTextureIndex(texIndexList)
                .addAll(list)
                .useGradient(true)
                .width(18));
        // lude来设置可视区域
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(list.get(0)).include(list.get(list.size() - 1)).build();
// 移动地图，所有latlng自适应显示。LatLngBounds与地图边缘10像素的填充区域


        if (list.size() < 6) {

            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(list.get(0), 19));
        } else {
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }

        addStartAndEndMarker(new LatLng(list.get(0).latitude, list.get(0).longitude),
                new LatLng(list.get(list.size() - 1).latitude, list.get(list.size() - 1).longitude));
    }


    /**
     * 添加起点和终点
     *
     * @param startPoint
     * @param endPoint
     */
    private void addStartAndEndMarker(LatLng startPoint, LatLng endPoint) {
        aMap.addMarker((new MarkerOptions())
                .position(startPoint).icon(BitmapDescriptorFactory.fromResource(R.drawable.start_add_icon))
                .title("\u8D77\u70B9"));

        aMap.addMarker((new MarkerOptions()).position(endPoint)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_add_icon)).title("\u7EC8\u70B9"));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_del:
                dismiss();
                break;
            case R.id.img_start_location:
                LocationHelper.getInstance().startLocation((Activity) mContext);
                break;
            case R.id.rly_commit_order:
                if (mBinding.tvCommitOrder.getText().toString().trim().equals("抢单")) {
                    if (isClick) {
                        isClick = false;


                        if ("1".equals(mOrderinfo.getData().getWorkType())) {//同城配送
                            commitOrder();
                        } else {
                            setEstablish();
                        }


                    }
                }
                break;
        }
    }

    /**
     * 立即沟通
     */
    public void setEstablish() {
        if (mOrderinfo == null) {
            return;
        }

        if (TextUtils.isEmpty(mOrderinfo.getData().getOrder())) {
            Toast.makeText(mContext, "订单号为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mOrderinfo.getData().getSendRingLetter())) {
            Toast.makeText(mContext, "发单人账号为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        showWaitDialog("正在建立沟通！！！");
        presenter.responsePart(mOrderinfo.getData().getOrder(),
                mOrderinfo.getData().getSendRingLetter(),
                MaiLiApplication.getInstance().mHuanxin,
                MaiLiApplication.getInstance().getUserInfo().getPhone());

    }

    /**
     * 提交订单
     */
    private void commitOrder() {
        showWaitDialog();
        UserInfoModel.UserInfoBean userInfo = MaiLiApplication.getInstance().getUserInfo();
        OrderInfoModel.DataBean data = mOrderinfo.getData();
        mPresenter.CommitOrders(data.getOrder(),
                userInfo.getPhone(),
                userInfo.getName(),
                data.getWorkCost() + "",
                data.getWorkTotalCost() + "",
                "0"
        );
    }


    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }

    };

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        isClick = true;
        MaiLiApplication.mOrderInfoModel = null;
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if (!errorModel.getStatus().equals("404")) {
                SpeedHelper.getInstance().startSpeed(errorModel.getInfo(), mSynListener);
            } else {
                Toast.makeText(MaiLiApplication.getInstance(), errorModel.getInfo(), Toast.LENGTH_SHORT).show();
            }
            dismiss();
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel info = (HttpSuccessModel) object;
            if ("1".equals(mOrderinfo.getData().getWorkType())) {
                Intent intent = new Intent("SocaketReceiver");//开始长连接和连续定位
                mContext.sendBroadcast(intent);
            }

          /*  if (DemoUtils.TypeToConfirOrder(Integer.valueOf(mOrderinfo.getData().getWorkType())) == 1) {//需要确认的订单
                SpeedHelper.getInstance().startSpeed("申请成功！", mSynListener);
            } else {

            }*/
            SpeedHelper.getInstance().startSpeed("抢单成功！", mSynListener);
            EventBus.getDefault().post(new UpdateNotice());
            dismiss();
            new OrderSuccessDialog(mContext, mOrderinfo).show();
        } else if (object instanceof PartTimeModel) {
            PartTimeModel mPartTimeModel = (PartTimeModel) object;
            if (mOrderinfo != null) {
                OrderInfoModel.DataBean data = mOrderinfo.getData();
                String address = "";
                if ("24".equals(mOrderinfo.getData().getWorkType())) {
                    address = DemoUtils.TypeToContent2(Integer.valueOf(mOrderinfo.getData().getWorkType()), mOrderinfo.getData().getWorkContent());
                } else {
                    address = data.getWorkPlace();
                }

                OrderContent orderContent = new OrderContent(
                        data.getOrder(),
                        data.getSendPhone(),
                        address,
                        data.getWorkCost(),
                        data.getWorkTotalCost(),
                        data.getOrderStatus(),
                        data.getNickName(),
                        data.getHeadUrl(),
                        MaiLiApplication.getInstance().getUserInfo().getNickname(),
                        MaiLiApplication.getInstance().getUserInfo().getHeadUrl(),
                        MaiLiApplication.getInstance().getUserInfo().getName(),
                        MaiLiApplication.getInstance().getUserInfo().getPhone(),
                        Integer.valueOf(data.getWorkType())
                );
                Intent intent = new Intent(mContext, ChatActivity.class);
                mContext.startActivity(intent.putExtra(EaseConstant.EXTRA_USER_ID, data.getSendRingLetter())
                        .putExtra("nick_name", data.getNickName())
                        .putExtra("is_welcome", true)
                        .putExtra("order", orderContent));
            }
            dismiss();
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    public void showWaitDialog() {

        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext, "抢单中...请稍后!");
        }
        mLoadingDialog.show();

    }

    public void showWaitDialog(String str) {

        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext, str);
        }
        mLoadingDialog.show();

    }

    public void hideWaitDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

}
