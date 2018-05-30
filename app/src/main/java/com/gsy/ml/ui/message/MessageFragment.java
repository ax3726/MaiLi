package com.gsy.ml.ui.message;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.FragmentMessageBinding;
import com.gsy.ml.model.message.OrderContent;
import com.gsy.ml.ui.common.BaseFragment;
import com.gsy.ml.ui.main.MessageActivity;
import com.gsy.ml.ui.views.InformationDialog;
import com.gsy.ml.ui.views.LongClickDialog;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;
import ml.gsy.com.library.utils.glide.GlideCircleTransform;

/**
 * Created by Administrator on 2017/4/18.
 */

public class MessageFragment extends BaseFragment {
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    private final static int MSG_REFRESH = 2;
    private FragmentMessageBinding mBinding;
    private LongClickDialog mLongClickDialog;
    private boolean isStick = false; //取消置顶

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initData() {
        super.initData();
        mBinding = (FragmentMessageBinding) vdb;
        EventBus.getDefault().register(this);
        mBinding.inHead.tvTitle.setText("消息");
        mBinding.inHead.llyLeft.setVisibility(View.INVISIBLE);
        setUpView();
    }

    private TextView mUnread_msg_number;

    public void updateMessageNoReadCount(int count) {

        if (mUnread_msg_number != null) {
            if (count > 0) {
                mUnread_msg_number.setVisibility(View.VISIBLE);
                mUnread_msg_number.setText(count + "");
            } else {
                mUnread_msg_number.setVisibility(View.GONE);
            }
        }
    }

    private void setUpView() {
        mLongClickDialog = new LongClickDialog(getActivity());

        conversationList.addAll(loadConversationList());
        mBinding.easeList.setCurPhone(MaiLiApplication.getInstance().getUserInfo().getPhone());//设置当前用户的手机号码
        mBinding.easeList.init(conversationList);

        View headView = View.inflate(aty, com.hyphenate.easeui.R.layout.ease_row_chat_history, null);
        LinearLayout list_itease_layout = (LinearLayout) headView.findViewById(R.id.list_itease_layout);
        ImageView avatar = (ImageView) headView.findViewById(R.id.avatar);
        mUnread_msg_number = (TextView) headView.findViewById(R.id.unread_msg_number);
        TextView name = (TextView) headView.findViewById(R.id.name);
        TextView type_name = (TextView) headView.findViewById(R.id.type_name);
        TextView message = (TextView) headView.findViewById(R.id.message);
        Glide.with(aty).load(R.drawable.message_logo)
                .transform(new GlideCircleTransform(aty))
                .placeholder(R.drawable.ease_default_avatar).into(avatar);
        name.setText("系统消息");
        message.setText("点击查看更多系统消息");
        type_name.setVisibility(View.GONE);
        mUnread_msg_number.setVisibility(View.GONE);

        list_itease_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MessageActivity.class));
            }
        });

        mBinding.easeList.addHeaderView(headView);

        mBinding.easeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = mBinding.easeList.getItem(position - 1);
                if (conversation != null) {
                    startActivity(new Intent(aty, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
                } else {
                    Toast.makeText(aty, "会话信息错误，请重试！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBinding.easeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                mLongClickDialog.show();
                final EMConversation conversation = mBinding.easeList.getItem(position - 1);
                if (conversation == null) {
                    Toast.makeText(aty, "会话信息错误，请重试！", Toast.LENGTH_SHORT).show();
                    return true;
                }
                EMMessage lastMessage = conversation.getLastMessage();
                String username = "";
                String order = conversation.getLastMessage().getStringAttribute("order", "");
                if (!TextUtils.isEmpty(order)) {
                    OrderContent mOrderContent = ParseJsonUtils.getBean((String) order, OrderContent.class);
                    if (mOrderContent != null) {

                        if (mOrderContent.getOrderPhone().equals(MaiLiApplication.getInstance().getUserInfo().getPhone())) {//是发单人
                            username = mOrderContent.getAcceptNickName();
                        } else {
                            username = mOrderContent.getSendNickName();
                        }
                    }
                }
                mLongClickDialog.setTextName(TextUtils.isEmpty(username) ? lastMessage.getFrom() : username);
                final String extField = conversation.getExtField();
                mLongClickDialog.setTextName1(TextUtils.isEmpty(extField) ? "置顶" : "取消置顶");
                mLongClickDialog.setLongClickListener(new LongClickDialog.LongClickListener() {
                    @Override
                    public void setStyle(int type, int postion) {
                        if (type == 0) {//置顶
                            if (TextUtils.isEmpty(extField)) {//置顶
                                EMClient.getInstance().chatManager().getConversation(conversation.conversationId()).setExtField(System.currentTimeMillis() + "");

                            } else {//取消置顶
                                EMClient.getInstance().chatManager().getConversation(conversation.conversationId()).setExtField("");
                            }
                            refresh();
                        } else {
                            sInformationDialog(conversation.conversationId());
                        }


                    }
                });
                return true;
            }
        });
        EMClient.getInstance().addConnectionListener(connectionListener);
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                if (MaiLiApplication.getInstance().isMainActivity()) {
                    refresh();
                }

            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });


    }


    public void sInformationDialog(final String measeage) {
        final InformationDialog informationDialog = new InformationDialog(aty);
        informationDialog.setTitle("提示");
        informationDialog.setMessage("您确定要删除该回话吗？");
        informationDialog.setPositiveButton("确定", new InformationDialog.IDialogClickListener() {
            @Override
            public void onDialogClick(Dialog dlg, View view) {
                //删除和某个user会话，如果需要保留聊天记录，传false
                EMClient.getInstance().chatManager().deleteConversation(measeage, false);
                refresh();
                dlg.dismiss();
            }
        });
        informationDialog.setNegativeButton("取消", new InformationDialog.IDialogClickListener() {
            @Override
            public void onDialogClick(Dialog dlg, View view) {
                dlg.dismiss();
            }
        });
        informationDialog.show();
    }


    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED
                    || error == EMError.USER_KICKED_BY_CHANGE_PASSWORD || error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
                //   isConflict = true;
            } else {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };


    /**
     * refresh ui
     */
    public void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
           /* if (mIupdateListener != null) {
                mIupdateListener.updateMessage(EMClient.getInstance().chatManager().getUnreadMessageCount());
            }*/

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateMessage(String message) {
        if ("更新消息".equals(message)) {
            refresh();
        }
    }

    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    //onConnectionDisconnected();
                    break;
                case 1:
                    // onConnectionConnected();
                    break;

                case MSG_REFRESH: {

                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    mBinding.easeList.refresh();
                    break;
                }
                default:
                    break;
            }
        }
    };


    /**
     * load conversation list
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {

        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {

                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        List<Pair<Long, EMConversation>> sortList1 = new ArrayList<Pair<Long, EMConversation>>();
        for (int i = 0; i < list.size(); i++) {
            EMConversation emConversation = list.get(i);
            sortList1.add(new Pair<Long, EMConversation>(TextUtils.isEmpty(emConversation.getExtField()) ?
                    0 : Long.valueOf(emConversation.getExtField()), emConversation));
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByzhidin(sortList1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list1 = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList1) {
            list1.add(sortItem.second);
        }
        return list1;
    }


    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByzhidin(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {
                int l1 = (int) ((TextUtils.isEmpty(con1.second.getExtField()) ? 0 : Long.valueOf(con1.second.getExtField())) / 1000);
                int l2 = (int) ((TextUtils.isEmpty(con2.second.getExtField()) ? 0 : Long.valueOf(con2.second.getExtField())) / 1000);
                return l2 - l1;
            }

        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            refresh();
        }
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {
                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private IupdateListener mIupdateListener;


    public void setIupdateListener(IupdateListener mIupdateListener) {
        this.mIupdateListener = mIupdateListener;
    }

    public interface IupdateListener {
        void updateMessage(int count);
    }
}
