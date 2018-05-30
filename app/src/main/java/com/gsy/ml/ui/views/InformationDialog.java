package com.gsy.ml.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gsy.ml.R;


/**
 * Created by CJJ on 2016/7/18.
 */
public class InformationDialog extends Dialog implements View.OnClickListener {


    private Context mContext;


    TextView tv_title;

    TextView tv_msg;

    TextView tv_negative;

    TextView tv_positive;
    TextView tv_hint;

    LinearLayout linearLayout;

    private IDialogClickListener onNegativeButtonClickListener;
    private IDialogClickListener onPositiveButtonClickListener;


    /**
     * 构造方法
     *
     * @param context 上下文对象
     * @param
     */
    public InformationDialog(Context context) {
        super(context, R.style.DialogBaseStyle);
        this.mContext = context;
        this.setContentView(R.layout.lib_information_dialog_layout);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tv_negative = (TextView) findViewById(R.id.tv_negative);
        tv_positive = (TextView) findViewById(R.id.tv_positive);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        tv_negative.setOnClickListener(this);
        tv_positive.setOnClickListener(this);
        tv_positive.setVisibility(View.GONE);
        tv_negative.setVisibility(View.GONE);
        WindowManager m = ((Activity) mContext).getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = (int) ((d.getWidth()) * 0.8);
        params.height = params.height;
        this.getWindow().setAttributes(params);
    }
    public void setNegativeVis(boolean bl) {
        tv_negative.setVisibility(bl ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置dialog的提示语
     *
     * @param hint dialog显示的提示语
     */
    public void setHint(String hint) {
        tv_hint.setVisibility(View.VISIBLE);
        tv_hint.setText(hint);
    }

    /**
     * 设置dialog的标题
     *
     * @param titleId dialog显示标题资源ID
     */
    @Override
    public void setTitle(int titleId) {
        tv_title.setText(titleId);
    }

    /**
     * 设置dialog的标题
     *
     * @param title dialog显示标题字符串
     */
    @Override
    public void setTitle(CharSequence title) {
        tv_title.setText(title);
    }

    /**
     * 设置dialog显示的消息内容
     *
     * @param message 消息内容
     */
    public void setMessage(CharSequence message) {
        tv_msg.setText(Html.fromHtml(message + ""));
    }


    /**
     * 设置dialog显示的消息内容
     *
     * @param messageId 消息内容资源ID
     */
    public void setMessage(int messageId) {
        tv_msg.setText(messageId);
    }

    /**
     * 设置dialog的否定按钮（一般用于用户否定操作，如：拒绝，否，取消等操作）
     *
     * @param text                          按钮显示文本（如果设为空字符串或者null，则该按钮不显示）
     * @param onNegativeButtonClickListener 用户点击该按钮后的回调对象，如果为null则点击按钮会关闭dialog
     */
    public void setNegativeButton(String text, IDialogClickListener onNegativeButtonClickListener) {
        this.onNegativeButtonClickListener = onNegativeButtonClickListener;
        this.tv_negative.setVisibility((text == null || text.equals("")) ? View.GONE : View.VISIBLE);
        this.tv_negative.setText(text);
        reLayoutButton();
    }

    /**
     * 设置dialog的活动按钮（一般用于用户确认操作，如：确定，同意，接受等操作）
     *
     * @param text                          按钮显示文本（如果设为空字符串或者null，则该按钮不显示）
     * @param onPositiveButtonClickListener 用户点击该按钮后的回调对象，如果为null则点击按钮会关闭dialog
     */
    public void setPositiveButton(String text, IDialogClickListener onPositiveButtonClickListener) {
        this.onPositiveButtonClickListener = onPositiveButtonClickListener;
        this.tv_positive.setVisibility((text == null || text.equals("")) ? View.GONE : View.VISIBLE);
        this.tv_positive.setText(text);
        reLayoutButton();
    }


    /**
     * 根据用户设置的Button,动态调整Button的位置和大小
     */
    private void reLayoutButton() {
        LinearLayout.LayoutParams layoutParams;
        if (tv_negative.getVisibility() == View.GONE && tv_positive.getVisibility() == View.VISIBLE) {
            layoutParams = (LinearLayout.LayoutParams) tv_positive.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 0);
            tv_positive.setLayoutParams(layoutParams);
        } else if (tv_negative.getVisibility() == View.VISIBLE && tv_positive.getVisibility() == View.GONE) {
            layoutParams = (LinearLayout.LayoutParams) tv_negative.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 0);
            tv_negative.setLayoutParams(layoutParams);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_positive:

                if (onPositiveButtonClickListener == null) {
                    this.dismiss();
                } else {
                    onPositiveButtonClickListener.onDialogClick(this, tv_positive);
                }
                break;
            case R.id.tv_negative://取消

                if (onNegativeButtonClickListener == null) {
                    this.dismiss();
                } else {
                    onNegativeButtonClickListener.onDialogClick(this, tv_negative);
                }
                break;
        }
    }

    /**
     * Dialog的统一点击事件接口，所有Dialog内的View点击事件
     * 都会调用该接口回调
     */
    public interface IDialogClickListener {
        /**
         * 对话框的view点击事件回调接口
         *
         * @param dlg  对话框
         * @param view 点击的view
         */
        void onDialogClick(Dialog dlg, View view);
    }

}
