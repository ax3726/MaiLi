package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.model.OrderContent;
import com.hyphenate.easeui.utils.EaseUtils;
import com.hyphenate.exceptions.HyphenateException;

import ml.gsy.com.library.utils.ParseJsonUtils;
import ml.gsy.com.library.utils.Utils;
import ml.gsy.com.library.utils.glide.GlideCircleTransform;

public class EaseChatRowOrder extends EaseChatRow {

    private TextView tv_title;
    private TextView tv_type;
    private TextView tv_time;
    private TextView tv_address;
    private TextView tv_name;
    private ImageView img_head;
    private LinearLayout lly_body;
    private TextView tv_pay_type;
    private TextView tv_jianzhi;

    public EaseChatRowOrder(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter, false);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_order_message : R.layout.ease_row_order_message, this);
    }

    @Override
    protected void onFindViewById() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_address = (TextView) findViewById(R.id.tv_address);
        img_head = (ImageView) findViewById(R.id.img_head);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_pay_type = (TextView) findViewById(R.id.tv_pay_type);
        tv_jianzhi = (TextView) findViewById(R.id.tv_jianzhi);
        lly_body = (LinearLayout) findViewById(R.id.lly_body);
    }

    @Override
    public void onSetUpView() {
      /*  EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
        // 设置内容
        contentView.setText(span, BufferType.SPANNABLE);

        handleTextMessage();*/
        String acceptTime = message.getStringAttribute("acceptTime","");
        String order  = message.getStringAttribute("order","");
        int payType = message.getIntAttribute("payType",1);

        if (!TextUtils.isEmpty(order)) {
            OrderContent mOrderContent = ParseJsonUtils.getBean((String) order, OrderContent.class);
            if (mOrderContent != null) {
                tv_type.setText(EaseUtils.TypeToOccupation(mOrderContent.getWorkTpye()));
                tv_address.setText(mOrderContent.getOrderAddress());

                if (message.direct() == EMMessage.Direct.SEND) {
                    tv_name.setText(mOrderContent.getAcceptNickName());
                    Glide.with(getContext()).load(mOrderContent.getAcceptHeadUrl()).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new GlideCircleTransform(getContext()))
                            .placeholder(R.drawable.ease_default_avatar).into(img_head);
                } else {
                    tv_name.setText(mOrderContent.getSendNickName());
                    Glide.with(getContext()).load(mOrderContent.getSendHeadUrl()).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new GlideCircleTransform(getContext()))
                            .placeholder(R.drawable.ease_default_avatar).into(img_head);
                }

                tv_time.setText(Utils.getTimeStyle5(TextUtils.isEmpty(acceptTime) ? 0 : Long.valueOf(acceptTime)));
            }
        }
        if (message.direct() == EMMessage.Direct.SEND) {
            tv_title.setText("你已指定对方接单");
            tv_jianzhi.setVisibility(View.GONE);
        } else {
            tv_title.setText("对方已指定你接单");
            tv_jianzhi.setVisibility(View.VISIBLE);
        }

        if (payType == 1) {
            tv_pay_type.setText("线上支付");
        } else {
            tv_pay_type.setText("线下支付");
        }
        lly_body.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.direct() == EMMessage.Direct.SEND) {
                    itemClickListener.onOrderClick(2);
                } else {
                    itemClickListener.onOrderClick(1);
                }
            }
        });

    }

    protected void handleTextMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
                case CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case FAIL:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS:
                    progressBar.setVisibility(View.VISIBLE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        } else {
            if (!message.isAcked() && message.getChatType() == ChatType.Chat) {
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        // TODO Auto-generated method stub

    }


}
