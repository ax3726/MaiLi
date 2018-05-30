package com.gsy.ml.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.gsy.ml.R;
import com.gsy.ml.databinding.DialogOrderSuccessBinding;
import com.gsy.ml.databinding.ItemOrderSucessToBinding;
import com.gsy.ml.model.main.OrderInfoModel;
import com.gsy.ml.ui.home.PartTimeJobActivity;
import com.gsy.ml.ui.utils.DemoUtils;

import static com.gsy.ml.ui.utils.DemoUtils.countDistance;

/**
 * Created by Administrator on 2017/4/29.
 */

public class OrderSuccessDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private DialogOrderSuccessBinding mBinding;

    private OrderInfoModel mOrderinfo = new OrderInfoModel();//订单信息

    public OrderSuccessDialog(@NonNull Context context, OrderInfoModel orderinfo) {
        super(context, R.style.DialogBaseStyle);
        mContext = context;
        mOrderinfo = orderinfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_order_success, null, false);
        this.setContentView(mBinding.getRoot());
        WindowManager m = ((Activity) mContext).getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) ((d.getWidth()) * 0.9), (int) ((d.getHeight()) * 0.75));
        layoutParams.setMargins(0, 0, 0, mContext.getResources().getDimensionPixelSize(R.dimen.PX90));
        mBinding.svContent.setLayoutParams(layoutParams);


        initListener();
        initView();

    }

    private void initListener() {
        mBinding.imgDel.setOnClickListener(this);
        mBinding.llyToOrderInfo.setOnClickListener(this);

    }

    private void initView() {
        if (mOrderinfo == null) {
            return;
        }
        OrderInfoModel.DataBean data = mOrderinfo.getData();
        mBinding.tvTitle.setText(data.getStartPlace() + DemoUtils.TypeToOccupation(Integer.valueOf(data.getWorkType())));
        if (Integer.valueOf(data.getWorkType()) == 1) {
            mBinding.tvMessage.setText("¥" + data.getWorkCost() + "\t距你" + countDistance(data.getStartJing(), data.getStartWei()));
        } else {
            mBinding.tvMessage.setText("¥" + data.getWorkCost() + "\t距你" + countDistance(data.getWorkJing(), data.getWorkWei()));

        }

     /*   if (DemoUtils.TypeToConfirOrder(Integer.valueOf(data.getWorkType())) == 1) {//需要确认的订单
            mBinding.imgSuccess.setImageResource(R.drawable.order_shen_success_lu);
        } else {

        }*/
        mBinding.imgSuccess.setImageResource(R.drawable.order_success_lu);
        String address = data.getStartProvince()
                + data.getStartCity()
                + data.getStartArea()
                + data.getStartPlace()
                + data.getStartDoorplate();
        mBinding.tvAddress.setText(address);


        String[] con = DemoUtils.TypeToContent(Integer.valueOf(data.getWorkType()), data.getWorkContent());
        mBinding.tvContent.setText(Html.fromHtml(con[0] + "<br/>"+con[1]));
        if (data.getWorkType().equals("1")) {//同城配送
            mBinding.tvName.setText("寄件人:" + data.getSendPeopleName());
            mBinding.tvPhone.setText(data.getSendPeoplePhone());

            mBinding.tvAddTitle.setText("取件地址");
            String acceptPeopleName = data.getReceiptPeopleName();
            int lenth = TextUtils.isEmpty(acceptPeopleName) ? 0 : acceptPeopleName.split(",").length;
            mBinding.llyAddTo.removeAllViews();
            if (lenth > 0) {
                String[] name = acceptPeopleName.split(",");
                String[] phone = data.getReceiptPeoplePhone().split(",");
                String[] workArea = data.getWorkArea().split(",");
                String[] workPlace = data.getWorkPlace().split(",");
                String[] workDoorplate = data.getWorkDoorplate().split(",");
                String[] workJing = data.getWorkJing().split(",");
                String[] workWei = data.getWorkWei().split(",");
                for (int i = 0; i < lenth; i++) {
                    ItemOrderSucessToBinding mAddBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_order_sucess_to, null, false);
                    mAddBinding.tvNameTitle.setText("收件人" + (i + 1) + ":" + name[i]);
                    mAddBinding.tvAddTitle.setText("收货地址" + (i + 1));
                    mAddBinding.tvPhone.setText(phone[i]);
                    String add = data.getWorkProvince()
                            + data.getWorkCity()
                            + workArea[i]
                            + workPlace[i]
                            + workDoorplate[i];
                    mAddBinding.tvAddress.setText(add);
                  /*  final AddressModel addressModel = new AddressModel();
                    addressModel.setAddress(add);
                    addressModel.setPoint(new LatLonPoint(Float.valueOf(workWei[i]), Float.valueOf(workJing[i])));

                    mAddBinding.imgAddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new ChooseMapPopuwindow(mContext, addressModel).showPopupWindow();//选择  高德  或者百度
                        }
                    });*/
                    mBinding.llyAddTo.addView(mAddBinding.getRoot());
                }
            }


        } else {
            mBinding.tvAddTitle.setText("工作地址");
            mBinding.tvName.setText("联系人:" + data.getSendPeople());
            mBinding.tvPhone.setText(data.getSendPhone());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_del:
                dismiss();
                break;
            case R.id.lly_to_order_info:
                if (mOrderinfo != null) {
                    dismiss();
                    mContext.startActivity(new Intent(mContext, PartTimeJobActivity.class)
                            .putExtra("order", mOrderinfo.getData().getOrder())
                            .putExtra("type", 1)
                            .putExtra("isShowAdd", true)
                            .putExtra("isShowPhone", true)

                    );
                }
                break;
           /*  case R.id.img_address:
               if (mOrderinfo != null) {
                    OrderMessageModel.DataBean data = mOrderinfo.getData();
                    AddressModel addressModel = new AddressModel();
                    addressModel.setAddress(data.getStartProvince()
                            + data.getStartCity()
                            + data.getStartArea()
                            + data.getStartPlace());
                    addressModel.setPoint(new LatLonPoint(Float.valueOf(data.getStartWei()), Float.valueOf(data.getStartJing())));
                    new ChooseMapPopuwindow(mContext, addressModel).showPopupWindow();//选择  高德  或者百度
                }
                break;*/
        }
    }
}
