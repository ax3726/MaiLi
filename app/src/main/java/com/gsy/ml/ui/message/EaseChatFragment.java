package com.gsy.ml.ui.message;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.EaseFragmentChatBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.main.AdvModel;
import com.gsy.ml.model.message.EaseChatFragmentModel;
import com.gsy.ml.model.message.EditPriceModel;
import com.gsy.ml.model.message.OrderContent;
import com.gsy.ml.model.message.OrderMessageModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.PartTimePresenter;
import com.gsy.ml.prestener.message.EaseChatPrestener;
import com.gsy.ml.ui.common.BaseFragment;
import com.gsy.ml.ui.common.WebViewActivity;
import com.gsy.ml.ui.home.PartTimeActivity;
import com.gsy.ml.ui.person.CheckActivity;
import com.gsy.ml.ui.person.OrderBillingActivity;
import com.gsy.ml.ui.person.OrderReceivingActivity;
import com.gsy.ml.ui.person.PartTimejobDetailsActivity;
import com.gsy.ml.ui.person.PaymentActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.views.InformContentPopuwindow;
import com.gsy.ml.ui.views.InformShieldPopuWindow;
import com.gsy.ml.ui.views.OfflineOnLineDialog;
import com.gsy.ml.ui.views.PaymentDialog;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.ui.EaseChatRoomListener;
import com.hyphenate.easeui.ui.EaseGroupListener;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.easeui.widget.EaseAlertDialog.AlertDialogUser;
import com.hyphenate.easeui.widget.EaseChatExtendMenu;
import com.hyphenate.easeui.widget.EaseChatInputMenu.ChatInputMenuListener;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView.EaseVoiceRecorderCallback;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowOrder;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowPrice;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ml.gsy.com.library.utils.ParseJsonUtils;
import ml.gsy.com.library.views.MarqueeView;

import static ml.gsy.com.library.utils.ParseJsonUtils.getjsonStr;

/**
 * you can new an EaseChatFragment to use or you can inherit it to expand.
 * You need call setArguments to pass chatType and userId
 * <br/>
 * <br/>
 * you can see ChatActivity in demo for your reference
 */
public class EaseChatFragment extends BaseFragment implements EMMessageListener, ILoadPVListener {
    protected static final String TAG = "EaseChatFragment";
    protected static final int REQUEST_CODE_MAP = 1;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;

    private EaseFragmentChatBinding mBinding;

    /**
     * params to fragment
     */
    protected Bundle fragmentArgs;
    protected int chatType;
    protected String toChatUsername;
    protected EMConversation conversation;
    protected InputMethodManager inputManager;
    protected ClipboardManager clipboard;

    protected Handler handler = new Handler();
    protected File cameraFile;

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ListView listView;
    private boolean isNewMsg = false;//有新的消息
    private boolean is_welcome = false;//第一次聊天
    private int mPayType = 1;//1线上交易 0线下交易
    protected boolean isloading;
    protected boolean haveMoreData = true;
    protected int pagesize = 20;
    protected GroupListener groupListener;
    protected ChatRoomListener chatRoomListener;
    protected EMMessage contextMenuMessage;
    static final int ITEM_XIANSHANG = 1;
    static final int ITEM_XIUGAI = 2;
    protected int[] itemStrings = {R.string.attach_xianshang, R.string.attach_xiugai};
    protected int[] itemdrawables = {R.drawable.message_xianshang_icon, R.drawable.message_editprice};
    protected int[] itemIds = {ITEM_XIANSHANG, ITEM_XIUGAI};
    private boolean isMessageListInited;
    protected MyItemClickListener extendMenuItemClickListener;
    protected boolean isRoaming = false;
    private ExecutorService fetchQueue;
    private String order;
    private String sendPhone;

    private boolean isBlock = false;
    private InformShieldPopuWindow mInformShieldPopuWindow; //功能选择
    private PaymentDialog mPaymentDialog; //支付
    private OfflineOnLineDialog mOfflineOnLineDialog; //线上线下Dialog
    protected InputMethodManager inputMethodManager;
    private PartTimePresenter presenter = new PartTimePresenter(this);
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1://更新
                    initOrder();
                    break;
            }
        }
    };
    private EaseChatPrestener presteners = new EaseChatPrestener(this);
    EaseChatFragmentModel model = new EaseChatFragmentModel();

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /* @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         return inflater.inflate(R.layout.ease_fragment_chat, container, false);
     }

     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, boolean roaming) {
         isRoaming = roaming;
         return inflater.inflate(R.layout.ease_fragment_chat, container, false);
     }
 */
    @Override
    public int getLayoutId() {
        return R.layout.ease_fragment_chat;
    }

    private OrderContent mOrderContent;//订单数据
    private String mOrderjson = "";//订单json字符串
    private String mNickName = "未知";//昵称

    /**
     * 、
     * 更新订单信息
     *
     * @param order
     */
    private void getOrderInfo(String order) {
        presenter.findOrderInfo(order);
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        mBinding = (EaseFragmentChatBinding) vdb;

        fragmentArgs = getArguments();
        // check if single chat or group chat
        chatType = fragmentArgs.getInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        // userId you are chat with or group id
        toChatUsername = fragmentArgs.getString(EaseConstant.EXTRA_USER_ID);
        mOrderContent = (OrderContent) fragmentArgs.getSerializable("order");
        mNickName = fragmentArgs.getString("nick_name");
        is_welcome = fragmentArgs.getBoolean("is_welcome", false);
        if (mOrderContent != null) {
            mOrderjson = getjsonStr(mOrderContent);
        }
        List<String> blackListFromServer = EMClient.getInstance().contactManager().getBlackListUsernames();
        if (blackListFromServer != null && blackListFromServer.size() > 0) {
            for (String black : blackListFromServer) {
                if (toChatUsername.equals(black)) {//黑名单
                    isBlock = true;
                }
            }
        }
        setChatFragmentHelper(new EaseChatFragmentHelper() {
            @Override
            public void onSetMessageAttributes(EMMessage message) {

            }

            @Override
            public void onEnterToChatDetails() {

            }

            @Override
            public void onAvatarClick(String username) {

            }

            @Override
            public void onAvatarLongClick(String username) {

            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                return false;
            }

            @Override
            public void onMessageBubbleLongClick(EMMessage message) {

            }

            @Override
            public boolean onExtendMenuItemClick(int itemId, View view) {
                return false;
            }

            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
                return new CustomChatRowProvider();
            }
        });
        initView();
        setUpView();


    }

    private void initOrder() {
        if (mOrderContent == null) {
            mBinding.llyOrder.setVisibility(View.GONE);
            return;
        }
        mBinding.inHead.tvTitle.setText(mNickName);
        mBinding.llyOrder.setVisibility(View.VISIBLE);
        mBinding.tvWorktype.setText(DemoUtils.TypeToOccupation(mOrderContent.getWorkTpye()));
        mBinding.tvAddress.setText(mOrderContent.getOrderAddress());

        if (MaiLiApplication.getInstance().getUserInfo().getPhone().equals(mOrderContent.getOrderPhone())) {//商家
            mBinding.tvXianshang.setVisibility(View.VISIBLE);
            mBinding.llyMoney.setVisibility(View.GONE);
            mBinding.inputMenu.setIsInput(true);
            if (mOrderContent.getOrderStatus() != 0 && mOrderContent.getOrderStatus() != 4) {
                mBinding.tvXianshang.setSelected(true);
                mBinding.tvXianshang.setTextColor(getResources().getColor(R.color.colorcccccc));
            } else {
                mBinding.tvXianshang.setSelected(false);
                mBinding.tvXianshang.setTextColor(getResources().getColor(R.color.color707070));
            }
            mBinding.llyOrder.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(aty, PartTimejobDetailsActivity.class)
                            .putExtra("order", mOrderContent.getOrder())
                            .putExtra("type", 2));
                }
            });
        } else {
            mBinding.tvXianshang.setVisibility(View.GONE);
            mBinding.llyMoney.setVisibility(View.VISIBLE);
            mBinding.tvMoneys.setText("¥" + mOrderContent.getWorkCost() + "");
            mBinding.inputMenu.setIsInput(false);

            mBinding.llyOrder.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(aty, PartTimeActivity.class)
                            .putExtra("order", mOrderContent.getOrder())
                    );
                }
            });
        }
        order = mOrderContent.getOrder();
        sendPhone = mOrderContent.getOrderPhone();
    }

    private void initTitle() {
        mInformShieldPopuWindow = new InformShieldPopuWindow(aty);
        mPaymentDialog = new PaymentDialog(aty);
        mOfflineOnLineDialog = new OfflineOnLineDialog(aty);
        if (isBlock) {//是黑名单
            mInformShieldPopuWindow.setText2("取消屏蔽消息");
        }

        //功能选择
        mInformShieldPopuWindow.setInformShieldListener(new InformShieldPopuWindow.InformShieldListener() {
            @Override
            public void onItem(int position) {
                if (position == 0) {
                    if (!TextUtils.isEmpty(mOrderContent.getOrder()) && !TextUtils.isEmpty(mOrderContent.getOrderPhone())) {
                        mInformShieldPopuWindow.setListener(new InformContentPopuwindow.InfromContentListener() {
                            @Override
                            public void onItem(int postion, String text) {
                                showWaitDialog();
                                switch (postion) {
                                    case 1:
                                        presenter.reportMessage(order,
                                                sendPhone,
                                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                                text);
                                        break;
                                    case 2:
                                        presenter.reportMessage(order,
                                                sendPhone,
                                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                                text);
                                        break;
                                    case 3:
                                        presenter.reportMessage(order,
                                                sendPhone,
                                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                                text);
                                        break;
                                    case 4:
                                        presenter.reportMessage(order,
                                                sendPhone,
                                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                                text);
                                        break;
                                    case 5:
                                        presenter.reportMessage(order,
                                                sendPhone,
                                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                                text);
                                        break;
                                    case 6:
                                        presenter.reportMessage(order,
                                                sendPhone,
                                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                                text);
                                        break;
                                    case 7:
                                        presenter.reportMessage(order,
                                                sendPhone,
                                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                                text);
                                        break;
                                    case 8:
                                        presenter.reportMessage(order,
                                                sendPhone,
                                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                                text);
                                        break;
                                    case 9:
                                        presenter.reportMessage(order,
                                                sendPhone,
                                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                                text);
                                        break;

                                }
                            }
                        });
                    } else {
                        Toast.makeText(aty, "订单错误！", Toast.LENGTH_SHORT).show();
                    }

                } else if (position == 1) {//黑名单
                    if (isBlock) {
                        try {
                            EMClient.getInstance().contactManager().removeUserFromBlackList(toChatUsername);
                            isBlock = false;
                            Toast.makeText(getContext(), "取消屏蔽消息成功！", Toast.LENGTH_SHORT).show();
                            mInformShieldPopuWindow.setText2("屏蔽消息");
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "取消屏蔽消息失败！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            EMClient.getInstance().contactManager().addUserToBlackList(toChatUsername, false);
                            isBlock = true;
                            Toast.makeText(getContext(), "屏蔽消息成功！", Toast.LENGTH_SHORT).show();
                            mInformShieldPopuWindow.setText2("取消屏蔽消息");
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "屏蔽消息失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (position == 2) {
                    if (mOrderContent != null) {
                        if (MaiLiApplication.getInstance().getUserInfo().getPhone().equals(mOrderContent.getOrderPhone())) {//商家
                            startActivity(new Intent(aty, CheckActivity.class).
                                    putExtra("phone", mOrderContent.getAcceptPhone())
                                    .putExtra("isshow", false)
                                    .putExtra("type", 2)
                            );

                        } else {
                            startActivity(new Intent(aty, CheckActivity.class).
                                    putExtra("phone", mOrderContent.getOrderPhone())
                                    .putExtra("isshow", false)
                                    .putExtra("type", 2)
                            );
                        }


                    }

                }
            }
        });

        //线上线下
        mOfflineOnLineDialog.setOfflineOnLineListener(new OfflineOnLineDialog.OfflineOnLineListener() {
            @Override
            public void setType(int type) {
                if (type == 1) {//线上
                    mPayType = 1;
                    if (mOrderContent != null) {
                        startActivity(new Intent(aty, PaymentActivity.class)
                                .putExtra("pay_type", mPayType)
                                .putExtra("order", mOrderContent));
                    } else {
                        Toast.makeText(aty, "订单数据有误!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mPayType = 0;
                    if (mOrderContent != null) {
                        startActivity(new Intent(aty, PaymentActivity.class)
                                .putExtra("pay_type", mPayType)
                                .putExtra("service_money", mServiceMoney)
                                .putExtra("order", mOrderContent));
                    } else {
                        Toast.makeText(aty, "订单数据有误!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        //支付
        mPaymentDialog.setmPaymentListener(new PaymentDialog.PaymentListener() {
            @Override
            public void setType(int type, int money, int moeny_type) {

            }
        });


    }

    /**
     * 初始化消息广告
     */
    private void initAdv() {


        if (MaiLiApplication.getInstance().mMessageAdvList != null) {
            mBinding.llyGuanggao.setVisibility(View.VISIBLE);
            //设置集合
            List<String> items = new ArrayList<>();

            for (AdvModel.DataBean.AdvertisListBean adv : MaiLiApplication.getInstance().mMessageAdvList) {
                items.add(adv.getContent());
            }
           /* items.add("恭喜188******24获得:败家娘们一枚");
            items.add("恭喜148******86获得:一念成佛");
            items.add("恭喜152******45获得:得到七龙珠");*/
            mBinding.mvBar1.startWithList(items);

            mBinding.mvBar1.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                @Override
                public void onItemClick(int position, TextView textView) {
                    if (!TextUtils.isEmpty(MaiLiApplication.getInstance().mMessageAdvList.get(position).getUrl())) {
                        startActivity(new Intent(getActivity(), WebViewActivity.class)
                                .putExtra("url", MaiLiApplication.getInstance().mMessageAdvList.get(position).getUrl()));
                    }
                }
            });
            mBinding.imgDel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBinding.llyGuanggao.setVisibility(View.GONE);
                }
            });

        } else {
            mBinding.llyGuanggao.setVisibility(View.GONE);
        }


    }

    /**
     * init view
     */
    protected void initView() {

        initTitle();
        initAdv();
        // hold to record voice
        //noinspection ConstantConditions


        // message list layout

        if (chatType != EaseConstant.CHATTYPE_SINGLE)
            mBinding.messageList.setShowUserNick(true);
//        mBinding.messageList.setAvatarShape(1);
        listView = mBinding.messageList.getListView();

        extendMenuItemClickListener = new MyItemClickListener();

        registerExtendMenuItem();
        // init input menu
        mBinding.inputMenu.init(null);
        mBinding.inputMenu.setChatInputMenuListener(new ChatInputMenuListener() {

            @Override
            public void onSendMessage(String content) {
                sendTextMessage(content);
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return mBinding.voiceRecorder.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
                    }
                });
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
                sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
            }
        });

        swipeRefreshLayout = mBinding.messageList.getSwipeRefreshLayout();
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (isRoaming) {
            fetchQueue = Executors.newSingleThreadExecutor();
        }
        mBinding.inHead.llyRight.setVisibility(View.VISIBLE);
        mBinding.inHead.imgRight.setVisibility(View.VISIBLE);
        mBinding.inHead.imgRight.setImageResource(R.drawable.meaagse_more_icon);
        mBinding.inHead.imgRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mInformShieldPopuWindow.showAsDropDown(mBinding.inHead.imgRight);
            }
        });

        mBinding.tvXianshang.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//线上支付
                if (!mBinding.tvXianshang.isSelected()) {
                    if (mOrderContent != null) {
                        mPayType = 1;
                        startActivity(new Intent(aty, PaymentActivity.class)
                                .putExtra("order", mOrderContent)
                                .putExtra("pay_type", mPayType)

                        );
                    } else {
                        Toast.makeText(aty, "订单数据有误!", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }

    protected void setUpView() {
        // mBinding.inHead.tvTitle.setText(toChatUsername);

        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            // set title
           /* if (EaseUserUtils.getUserInfo(toChatUsername) != null) {
                EaseUser user = EaseUserUtils.getUserInfo(toChatUsername);
                if (user != null) {
                    mBinding.inHead.tvTitle.setText(user.getNick());
                }
            }*/

        } else {

            if (chatType == EaseConstant.CHATTYPE_GROUP) {
                //group chat
                EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
                if (group != null)
                    //  mBinding.inHead.tvTitle.setText(group.getGroupName());
                    // listen the event that user moved out group or group is dismissed
                    groupListener = new GroupListener();
                EMClient.getInstance().groupManager().addGroupChangeListener(groupListener);
            } else {
                chatRoomListener = new ChatRoomListener();
                EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomListener);
                onChatRoomViewCreation();
            }

        }
        if (chatType != EaseConstant.CHATTYPE_CHATROOM) {
            onConversationInit();
            onMessageListInit();
        }

        mBinding.inHead.llyLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        /*titleBar.setRightLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                    emptyHistory();
                } else {
                    toGroupDetails();
                }
            }
        });*/

        setRefreshLayoutListener();

        // show forward message if the message is not null
        String forward_msg_id = getArguments().getString("forward_msg_id");
        if (forward_msg_id != null) {
            forwardMessage(forward_msg_id);
        }
    }

    /**
     * register extend menu, item id need > 3 if you override this method and keep exist item
     */
    protected void registerExtendMenuItem() {
        for (int i = 0; i < itemStrings.length; i++) {
            mBinding.inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
        }
    }


    protected void onConversationInit() {
        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        if (conversation == null) {
            return;
        }

        if (conversation.getUnreadMsgCount() > 0) {//有未读消息
            isNewMsg = true;
        }
        if (mOrderContent == null) {
            try {
                mOrderjson = conversation.getLastMessage().getStringAttribute("order");
                if (!TextUtils.isEmpty(mOrderjson)) {
                    mOrderContent = ParseJsonUtils.getBean((String) mOrderjson, OrderContent.class);
                    if (mOrderContent.getOrderPhone().equals(MaiLiApplication.getInstance().getUserInfo().getPhone())) {//是发单人
                        mNickName = mOrderContent.getAcceptNickName();
                    } else {
                        mNickName = mOrderContent.getSendNickName();
                    }
                    getOrderInfo(mOrderContent.getOrder());
                }
                initOrder();
            } catch (HyphenateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (is_welcome) {//是否是从兼职页面进入
                EMMessage lastMessage = conversation.getLastMessage();
                if (lastMessage != null) {
                    try {
                        String orderjson = conversation.getLastMessage().getStringAttribute("order", "");
                        if (!TextUtils.isEmpty(orderjson)) {
                            OrderContent orderContent = ParseJsonUtils.getBean((String) orderjson, OrderContent.class);
                            if (!mOrderContent.getOrder().equals(orderContent.getOrder())) {//与上一次的订单不一样
                                sendTextMessage("您好，我对这份兼职很感兴趣，如果方便，希望能和您聊聊!");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {//第一次进入
                    sendTextMessage("您好，我对这份兼职很感兴趣，如果方便，希望能和您聊聊!");
                }

            }
            initOrder();
            getOrderInfo(mOrderContent.getOrder());
        }
        conversation.markAllMessagesAsRead();
        // the number of messages loaded into conversation is getChatOptions().getNumberOfMessagesLoaded
        // you can change this number

        if (!isRoaming) {
            final List<EMMessage> msgs = conversation.getAllMessages();
            int msgCount = msgs != null ? msgs.size() : 0;
            if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
                String msgId = null;
                if (msgs != null && msgs.size() > 0) {
                    msgId = msgs.get(0).getMsgId();
                }
                conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
            }
        } else {
            fetchQueue.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().chatManager().fetchHistoryMessages(
                                toChatUsername, EaseCommonUtils.getConversationType(chatType), pagesize, "");
                        final List<EMMessage> msgs = conversation.getAllMessages();
                        int msgCount = msgs != null ? msgs.size() : 0;
                        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
                            String msgId = null;
                            if (msgs != null && msgs.size() > 0) {
                                msgId = msgs.get(0).getMsgId();
                            }
                            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
                        }
                        mBinding.messageList.refreshSelectLast();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    protected void onMessageListInit() {
        mBinding.messageList.init(toChatUsername, chatType, chatFragmentHelper != null ?
                chatFragmentHelper.onSetCustomChatRowProvider() : null);
        setListItemClickListener();

        mBinding.messageList.getListView().setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                mBinding.inputMenu.hideExtendMenuContainer();
                return false;
            }
        });

        isMessageListInited = true;
    }

    protected void setListItemClickListener() {
        mBinding.messageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {

            @Override
            public void onUserAvatarClick(String username) {
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onAvatarClick(username);
                }
            }

            @Override
            public void onUserAvatarLongClick(String username) {
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onAvatarLongClick(username);
                }
            }

            @Override
            public void onOrderClick(int type) {
                if (type == 1) {//我的接单
                    startActivity(new Intent(aty, OrderReceivingActivity.class).putExtra("type", 1));
                } else {
                    startActivity(new Intent(aty, OrderBillingActivity.class).putExtra("type", 1));
                }
            }

            @Override
            public void onResendClick(final EMMessage message) {
                new EaseAlertDialog(getActivity(), R.string.resend, R.string.confirm_resend, null, new AlertDialogUser() {
                    @Override
                    public void onResult(boolean confirmed, Bundle bundle) {
                        if (!confirmed) {
                            return;
                        }
                        resendMessage(message);
                    }
                }, true).show();
            }

            @Override
            public void onBubbleLongClick(EMMessage message) {
                contextMenuMessage = message;
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onMessageBubbleLongClick(message);
                }
            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                if (chatFragmentHelper == null) {
                    return false;
                }
                return chatFragmentHelper.onMessageBubbleClick(message);
            }

        });
    }

    protected void setRefreshLayoutListener() {
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (!isRoaming) {
                            loadMoreLocalMessage();
                        } else {
                            loadMoreRoamingMessages();
                        }
                    }
                }, 600);
            }
        });
    }

    private void loadMoreLocalMessage() {
        if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData&&conversation!=null) {
            List<EMMessage> messages;
            try {
                messages = conversation.loadMoreMsgFromDB(conversation.getAllMessages().size() == 0 ? "" : conversation.getAllMessages().get(0).getMsgId(),
                        pagesize);
            } catch (Exception e1) {
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            if (messages.size() > 0) {
                mBinding.messageList.refreshSeekTo(messages.size() - 1);
                if (messages.size() != pagesize) {
                    haveMoreData = false;
                }
            } else {
                haveMoreData = false;
            }

            isloading = false;
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages),
                    Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadMoreRoamingMessages() {
        if (!haveMoreData) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages),
                    Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        if (fetchQueue != null) {
            fetchQueue.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<EMMessage> messages = conversation.getAllMessages();
                        EMClient.getInstance().chatManager().fetchHistoryMessages(
                                toChatUsername, EaseCommonUtils.getConversationType(chatType), pagesize,
                                (messages != null && messages.size() > 0) ? messages.get(0).getMsgId() : "");
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    } finally {
                        Activity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadMoreLocalMessage();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // capture new image
                if (cameraFile != null && cameraFile.exists())
                    sendImageMessage(cameraFile.getAbsolutePath());
            } else if (requestCode == REQUEST_CODE_LOCAL) { // send local image
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            } else if (requestCode == REQUEST_CODE_MAP) { // location
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String locationAddress = data.getStringExtra("address");
                if (locationAddress != null && !locationAddress.equals("")) {
                    sendLocationMessage(latitude, longitude, locationAddress);
                } else {
                    Toast.makeText(getActivity(), R.string.unable_to_get_loaction, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isMessageListInited)
            mBinding.messageList.refresh();
        EaseUI.getInstance().pushActivity(getActivity());
        // register the event listener when enter the foreground
        EMClient.getInstance().chatManager().addMessageListener(this);

        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            EaseAtMessageHelper.get().removeAtMeGroup(toChatUsername);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // unregister this event listener when this activity enters the
        // background
        EMClient.getInstance().chatManager().removeMessageListener(this);

        // remove activity from foreground activity list
        EaseUI.getInstance().popActivity(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (groupListener != null) {
            EMClient.getInstance().groupManager().removeGroupChangeListener(groupListener);
        }

        if (chatRoomListener != null) {
            EMClient.getInstance().chatroomManager().removeChatRoomListener(chatRoomListener);
        }

        if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
        }
    }

    public void onBackPressed() {
        if (mBinding.inputMenu.onBackPressed()) {
            getActivity().finish();
            if (chatType == EaseConstant.CHATTYPE_GROUP) {
                EaseAtMessageHelper.get().removeAtMeGroup(toChatUsername);
                EaseAtMessageHelper.get().cleanToAtUserList();
            }
            if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
                EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
            }
        }
    }

    protected void onChatRoomViewCreation() {
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "", "Joining......");
        EMClient.getInstance().chatroomManager().joinChatRoom(toChatUsername, new EMValueCallBack<EMChatRoom>() {

            @Override
            public void onSuccess(final EMChatRoom value) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getActivity().isFinishing() || !toChatUsername.equals(value.getId()))
                            return;
                        pd.dismiss();
                        EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(toChatUsername);
                        if (room != null) {
                            mBinding.inHead.tvTitle.setText(room.getName());
                            EMLog.d(TAG, "join room success : " + room.getName());
                        } else {
                            mBinding.inHead.tvTitle.setText(toChatUsername);
                        }
                        onConversationInit();
                        onMessageListInit();
                    }
                });
            }

            @Override
            public void onError(final int error, String errorMsg) {
                // TODO Auto-generated method stub
                EMLog.d(TAG, "join room failure : " + error);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                });
                getActivity().finish();
            }
        });
    }

    // implement methods in EMMessageListener
    @Override
    public void onMessageReceived(List<EMMessage> messages) {


        EMMessage price_message = null;
        EMMessage accept_message = null;
        for (EMMessage message : messages) {
            String username = null;
            Log.e("message", "order:" + message.getStringAttribute("order", ""));
            Log.e("message", "orderPrice:" + message.getStringAttribute("orderPrice", ""));
            if (!TextUtils.isEmpty(message.getStringAttribute("orderPrice", ""))) {//有价格更新
                price_message = message;
            } else if (!TextUtils.isEmpty(message.getStringAttribute("acceptTime", ""))) //接单
            {
                accept_message = message;
            }

            // group message
            if (message.getChatType() == ChatType.GroupChat || message.getChatType() == ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // single chat message
                username = message.getFrom();
            }

            // if the message is for current conversation
            if (username.equals(toChatUsername) || message.getTo().equals(toChatUsername) || message.conversationId().equals(toChatUsername)) {
                mBinding.messageList.refreshSelectLast();
                EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
                conversation.markMessageAsRead(message.getMsgId());
            } else {
                EaseUI.getInstance().getNotifier().onNewMsg(message);
            }
        }
        if (price_message != null) {//更新价格
            mOrderContent.setWorkCost(Double.valueOf(price_message.getStringAttribute("orderPrice", "")));
            mOrderjson = ParseJsonUtils.getjsonStr(mOrderContent);
            mHandler.sendEmptyMessage(1);
        } else if (accept_message != null) {
            mOrderContent.setOrderStatus(1);
            mOrderjson = ParseJsonUtils.getjsonStr(mOrderContent);
            mHandler.sendEmptyMessage(1);
        }

    }


    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {

    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        if (isMessageListInited) {
            mBinding.messageList.refresh();
        }
    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {
        if (isMessageListInited) {
            mBinding.messageList.refresh();
        }
    }

    @Override
    public void onMessageRecalled(List<EMMessage> messages) {
        if (isMessageListInited) {
            mBinding.messageList.refresh();
        }
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object change) {
        if (isMessageListInited) {
            mBinding.messageList.refresh();
        }
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel model = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), model.getInfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel model = (HttpSuccessModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), "举报成功！", Toast.LENGTH_SHORT).show();
        } else if (object instanceof OrderMessageModel) {
            OrderMessageModel info = (OrderMessageModel) object;//更新订单状态
            if (mOrderContent != null && info.getData() != null) {
                mOrderContent.setWorkTotalCost(info.getData().getWorkTotalCost());
                mOrderContent.setWorkCost(info.getData().getWorkCost());
                mOrderContent.setOrderStatus(info.getData().getOrderStatus());
                mOrderjson = ParseJsonUtils.getjsonStr(mOrderContent);
                initOrder();
            }
        } else if (object instanceof EaseChatFragmentModel) {
            EaseChatFragmentModel model = (EaseChatFragmentModel) object;
            getModel(model);

        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    private double mServiceMoney = 0;//信息费


    public void getModel(EaseChatFragmentModel model) {
        if (model == null && model.getData() == null) {
            return;
        }
        mServiceMoney = model.getData().getDiscountPrice();
        String text1 = String.valueOf(model.getData().getServiceMoney());
        String text2 = String.valueOf(model.getData().getDiscountPrice());
        mOfflineOnLineDialog.show();
        mOfflineOnLineDialog.setText(text1);
        mOfflineOnLineDialog.setMessage("<font color='#ff0000'>￥" + text2 + "</font><font color='#ffffff'>)</font>");
    }

    /**
     * handle the click event for extend menu
     */
    class MyItemClickListener implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener {

        @Override
        public void onClick(int itemId, View view) {
            if (chatFragmentHelper != null) {
                if (chatFragmentHelper.onExtendMenuItemClick(itemId, view)) {
                    return;
                }
            }
            //  selectPicFromCamera();
            //   selectPicFromLocal();
            switch (itemId) {
                case ITEM_XIANSHANG:
                    if (!mBinding.tvXianshang.isSelected()) {
                        showWaitDialog();
                        presteners.messageFee(MaiLiApplication.getInstance().getUserInfo().getPhone(), MaiLiApplication.getInstance().getUserPlace().getCity());

                    } else {
                        showToast("订单已交易");
                    }

                    break;
                case ITEM_XIUGAI:
                    if (mOrderContent != null) {
                        if (mOrderContent.getOrderStatus() == 0) {
                            startActivity(new Intent(aty, PartTimejobDetailsActivity.class)
                                    .putExtra("order", mOrderContent.getOrder())
                                    .putExtra("type", 2)
                                    .putExtra("is_price_edit", true));
                        } else {
                            showToast("当前订单状态下不能修改价格哦!");
                        }
                    } else {
                        showToast("订单信息有误!");
                    }

                    break;
                default:
                    break;
            }
        }

    }

    /**
     * input @
     *
     * @param username
     */

    protected void inputAtUsername(String username, boolean autoAddAtSymbol) {
        if (EMClient.getInstance().getCurrentUser().equals(username) ||
                chatType != EaseConstant.CHATTYPE_GROUP) {
            return;
        }
        EaseAtMessageHelper.get().addAtUser(username);
        EaseUser user = EaseUserUtils.getUserInfo(username);
        if (user != null) {
            username = user.getNick();
        }
        if (autoAddAtSymbol)
            mBinding.inputMenu.insertText("@" + username + " ");
        else
            mBinding.inputMenu.insertText(username + " ");
    }


    /**
     * input @
     *
     * @param username
     */
    protected void inputAtUsername(String username) {
        inputAtUsername(username, true);
    }


    //send message
    protected void sendTextMessage(String content) {
        if (EaseAtMessageHelper.get().containsAtUsername(content)) {
            sendAtMessage(content);
        } else {
            isNewMsg = true;
            EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
            sendMessage(message);
        }
    }

    /**
     * send @ message, only support group chat message
     *
     * @param content
     */
    @SuppressWarnings("ConstantConditions")
    private void sendAtMessage(String content) {
        if (chatType != EaseConstant.CHATTYPE_GROUP) {
            EMLog.e(TAG, "only support group chat message");
            return;
        }
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
        if (EMClient.getInstance().getCurrentUser().equals(group.getOwner()) && EaseAtMessageHelper.get().containsAtAll(content)) {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG, EaseConstant.MESSAGE_ATTR_VALUE_AT_MSG_ALL);
        } else {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG,
                    EaseAtMessageHelper.get().atListToJsonArray(EaseAtMessageHelper.get().getAtMessageUsernames(content)));
        }
        sendMessage(message);

    }


    protected void sendBigExpressionMessage(String name, String identityCode) {
        EMMessage message = EaseCommonUtils.createExpressionMessage(toChatUsername, name, identityCode);
        sendMessage(message);
    }

    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        sendMessage(message);
    }

    protected void sendImageMessage(String imagePath) {
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        sendMessage(message);
    }

    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        sendMessage(message);
    }

    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        sendMessage(message);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        sendMessage(message);
    }


    protected void sendMessage(EMMessage message) {
        if (message == null) {
            return;
        }
        if (!TextUtils.isEmpty(mOrderjson)) {
            EaseUser easeUser = EaseUI.getInstance().getEaseUser();
            message.setAttribute("order", mOrderjson);
            message.setAttribute("nick_name", easeUser.getInitialLetter());
            message.setAttribute("phone", easeUser.getPhone());
            message.setAttribute("head_url", easeUser.getAvatar());
        }

        if (chatFragmentHelper != null) {
            //set extension
            chatFragmentHelper.onSetMessageAttributes(message);
        }
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            message.setChatType(ChatType.GroupChat);
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            message.setChatType(ChatType.ChatRoom);
        }
        //send message
        EMClient.getInstance().chatManager().sendMessage(message);
        //refresh ui
        if (isMessageListInited) {
            mBinding.messageList.refreshSelectLast();
        }
    }


    public void resendMessage(EMMessage message) {
        message.setStatus(EMMessage.Status.CREATE);
        EMClient.getInstance().chatManager().sendMessage(message);
        mBinding.messageList.refresh();
    }

    //===================================================================================


    /**
     * send image
     *
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendImageMessage(file.getAbsolutePath());
        }

    }

    /**
     * send file
     *
     * @param uri
     */
    protected void sendFileByUri(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;

            try {
                cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(getActivity(), R.string.File_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }
        sendFileMessage(filePath);
    }

    /**
     * capture new image
     */
    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isSdcardExist()) {
            Toast.makeText(getActivity(), R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser()
                + System.currentTimeMillis() + ".jpg");
        //noinspection ResultOfMethodCallIgnored
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * select local image
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }


    /**
     * clear the conversation history
     */
    protected void emptyHistory() {
        String msg = getResources().getString(R.string.Whether_to_empty_all_chats);
        new EaseAlertDialog(getActivity(), null, msg, null, new AlertDialogUser() {

            @Override
            public void onResult(boolean confirmed, Bundle bundle) {
                if (confirmed) {
                    if (conversation != null) {
                        conversation.clearAllMessages();
                    }
                    mBinding.messageList.refresh();
                    haveMoreData = true;
                }
            }
        }, true).show();
    }

    /**
     * open group detail
     */
    protected void toGroupDetails() {
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
            if (chatFragmentHelper != null) {
                chatFragmentHelper.onEnterToChatDetails();
            }
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            if (chatFragmentHelper != null) {
                chatFragmentHelper.onEnterToChatDetails();
            }
        }
    }

    /**
     * hide
     */
    protected void hideKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * forward message
     *
     * @param forward_msg_id
     */
    protected void forwardMessage(String forward_msg_id) {
        final EMMessage forward_msg = EMClient.getInstance().chatManager().getMessage(forward_msg_id);
        EMMessage.Type type = forward_msg.getType();
        switch (type) {
            case TXT:
                if (forward_msg.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                    sendBigExpressionMessage(((EMTextMessageBody) forward_msg.getBody()).getMessage(),
                            forward_msg.getStringAttribute(EaseConstant.MESSAGE_ATTR_EXPRESSION_ID, null));
                } else {
                    // get the content and send it
                    String content = ((EMTextMessageBody) forward_msg.getBody()).getMessage();
                    sendTextMessage(content);
                }
                break;
            case IMAGE:
                // send image
                String filePath = ((EMImageMessageBody) forward_msg.getBody()).getLocalUrl();
                if (filePath != null) {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        // send thumb nail if original image does not exist
                        filePath = ((EMImageMessageBody) forward_msg.getBody()).thumbnailLocalPath();
                    }
                    sendImageMessage(filePath);
                }
                break;
            default:
                break;
        }

        if (forward_msg.getChatType() == ChatType.ChatRoom) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(forward_msg.getTo());
        }
    }

    /**
     * listen the group event
     */
    class GroupListener extends EaseGroupListener {

        @Override
        public void onUserRemoved(final String groupId, String groupName) {
            getActivity().runOnUiThread(new Runnable() {

                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(getActivity(), R.string.you_are_group, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }

        @Override
        public void onGroupDestroyed(final String groupId, String groupName) {
            // prompt group is dismissed and finish this activity
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(getActivity(), R.string.the_current_group_destroyed, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }
    }

    /**
     * listen chat room event
     */
    class ChatRoomListener extends EaseChatRoomListener {

        @Override
        public void onChatRoomDestroyed(final String roomId, final String roomName) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (roomId.equals(toChatUsername)) {
                        Toast.makeText(getActivity(), R.string.the_current_chat_room_destroyed, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }

        @Override
        public void onRemovedFromChatRoom(final String roomId, final String roomName, final String participant) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (roomId.equals(toChatUsername)) {
                        Toast.makeText(getActivity(), R.string.quiting_the_chat_room, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }


        @Override
        public void onMemberJoined(final String roomId, final String participant) {
            if (roomId.equals(toChatUsername)) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "member join:" + participant, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        @Override
        public void onMemberExited(final String roomId, final String roomName, final String participant) {
            if (roomId.equals(toChatUsername)) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "member exit:" + participant, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }


    }

    protected EaseChatFragmentHelper chatFragmentHelper;

    public void setChatFragmentHelper(EaseChatFragmentHelper chatFragmentHelper) {
        this.chatFragmentHelper = chatFragmentHelper;
    }

    public interface EaseChatFragmentHelper {
        /**
         * set message attribute
         */
        void onSetMessageAttributes(EMMessage message);

        /**
         * enter to chat detail
         */
        void onEnterToChatDetails();

        /**
         * on avatar clicked
         *
         * @param username
         */
        void onAvatarClick(String username);

        /**
         * on avatar long pressed
         *
         * @param username
         */
        void onAvatarLongClick(String username);

        /**
         * on message bubble clicked
         */
        boolean onMessageBubbleClick(EMMessage message);

        /**
         * on message bubble long pressed
         */
        void onMessageBubbleLongClick(EMMessage message);

        /**
         * on extend menu item clicked, return true if you want to override
         *
         * @param view
         * @param itemId
         * @return
         */
        boolean onExtendMenuItemClick(int itemId, View view);

        /**
         * on set custom chat row provider
         *
         * @return
         */
        EaseCustomChatRowProvider onSetCustomChatRowProvider();
    }

    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            return 7;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                //voice call
                if (!TextUtils.isEmpty(message.getStringAttribute("acceptTime", ""))) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? 1 : 2;
                } else if (!TextUtils.isEmpty(message.getStringAttribute("orderPrice", ""))) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? 3 : 4;
                }
                //end of red packet code
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {
                // voice call or video call
                if (!TextUtils.isEmpty(message.getStringAttribute("acceptTime", ""))) {//交易成功!
                    return new EaseChatRowOrder(getActivity(), message, position, adapter);
                } else if (!TextUtils.isEmpty(message.getStringAttribute("orderPrice", ""))) {//修改价格
                    return new EaseChatRowPrice(getActivity(), message, position, adapter);
                }

                //end of red packet code
            }
            return null;
        }

    }

    /**
     * 交易成功
     *
     * @param orderContent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateAccept(OrderContent orderContent) {
        isNewMsg = true;
        EMMessage message = EMMessage.createTxtSendMessage("接单成功！", toChatUsername);
        if (message != null) {
            if (mOrderContent != null) {
                mOrderContent.setOrderStatus(1);//改变订单状态
                mOrderjson = getjsonStr(mOrderContent);
            }
            initOrder();
            message.setAttribute("acceptTime", String.valueOf(System.currentTimeMillis()));
            message.setAttribute("payType", mPayType);
        }
        sendMessage(message);
    }

    /**
     * 价格修改成功
     *
     * @param editPriceModel
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdatePrice(EditPriceModel editPriceModel) {
        isNewMsg = true;
        mOrderContent.setWorkCost(editPriceModel.getPrice());
        mOrderjson = ParseJsonUtils.getjsonStr(mOrderContent);
        mHandler.sendEmptyMessage(1);
        EMMessage message = EMMessage.createTxtSendMessage("价格修改成功！", toChatUsername);
        if (message != null) {
            message.setAttribute("orderPrice", String.valueOf(editPriceModel.getPrice()));
        }
        sendMessage(message);
    }

    @Override
    public void onDestroyView() {
        if (isNewMsg) {
            EventBus.getDefault().post("更新消息");//更新首页的消息
        }
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
