package com.hyphenate.easeui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.easeui.R;

/**
 * Created by Administrator on 2017/9/6.
 */

public class UsefulExpressionsDialog extends Dialog implements View.OnClickListener {
    private final Context mContext;
    private ImageView imgShut;
    private EditText et;
    private TextView tvPreserve;
    private String content;
    private TextView tv_send;
    private TextView tvQuantity;
    private int num;


    public UsefulExpressionsDialog(@NonNull Context context) {
        super(context, R.style.DialogBaseStyle);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_useful_expressions, null);
        this.setContentView(inflate);
        WindowManager m = ((Activity) mContext).getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = (int) ((d.getWidth()) * 0.9);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(params);

        imgShut = (ImageView) inflate.findViewById(R.id.img_shut);
        tvPreserve = (TextView) inflate.findViewById(R.id.tv_preserve);
        et = (EditText) inflate.findViewById(R.id.et_useful_expressions);
        tv_send = (TextView) inflate.findViewById(R.id.tv_send);
        tvQuantity = (TextView) inflate.findViewById(R.id.tv_quantity);


        initListener();
    }

    public void initListener() {
        imgShut.setOnClickListener(this);
        tvPreserve.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        et.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            num = et.getText().toString().length();
            tvQuantity.setText(num + "");
            if (num > 200) {
                tvQuantity.setTextColor(mContext.getResources().getColor(R.color.holo_red_light));
            } else {
                tvQuantity.setTextColor(mContext.getResources().getColor(R.color.recall_white_54));
            }
        }
    };

    private int mPosition = -1;

    public void setText(String str, int positon) {
        et.setText(str);
        mPosition = positon;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_preserve) {
            if (TextUtils.isEmpty(et.getText().toString().trim())) {
                Toast.makeText(mContext, "常用语不能为空！" +
                        "" +
                        "！", Toast.LENGTH_SHORT).show();
                return;
            } else if (num > 200) {
                Toast.makeText(mContext, "最多只能输入200字哦～", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (mUsefulExpressionsListener != null) {
                    if (mPosition != -1) {
                        mUsefulExpressionsListener.editItem(0, mPosition, et.getText().toString().trim());
                    } else {
                        mUsefulExpressionsListener.setItem(0, et.getText().toString().trim());
                    }
                }
                et.setText("");
                dismiss();
            }
        } else if (i == R.id.img_shut) {
            dismiss();
            et.setText("");
        } else if (i == R.id.tv_send) {
            if (TextUtils.isEmpty(et.getText().toString().trim())) {
                Toast.makeText(mContext, "常用语不能为空！", Toast.LENGTH_SHORT).show();
                return;
            } else if (num > 200) {
                Toast.makeText(mContext, "最多只能输入200字哦～", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (mUsefulExpressionsListener != null) {
                    if (mPosition != -1) {
                        mUsefulExpressionsListener.editItem(1, mPosition, et.getText().toString().trim());
                    } else {
                        mUsefulExpressionsListener.setItem(1, et.getText().toString().trim());
                    }
                }
                et.setText("");
                dismiss();
            }
        }
    }

    protected UsefulExpressionsListener mUsefulExpressionsListener;//

    public void setUsefulExpressionsListener(UsefulExpressionsListener mUsefulExpressionsListener) {
        this.mUsefulExpressionsListener = mUsefulExpressionsListener;
    }

    public interface UsefulExpressionsListener {
        void setItem(int type, String text);

        void editItem(int type, int position, String text);
    }
}
