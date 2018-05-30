package com.gsy.ml.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gsy.ml.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/12.
 */

public class Code extends LinearLayout implements TextWatcher, View.OnKeyListener {
    //验证码的位数
    private int codeNumber;
    //两个验证码之间的距离
    private int codeSpace;
    //验证码边框的边长
    private int lengthSide;
    //验证码的大小
    private float textSize;
    //字体颜色
    private int textColor = Color.BLACK;

    private int inputType = 2;

    private LinearLayout.LayoutParams mEditparams;

    private LinearLayout.LayoutParams mViewparams;

    private Context mContext;

    private List<EditText> mEditTextList = new ArrayList<>();

    private int currentPosition = 0;

    public Code(Context context) {
        super(context);
    }

    public Code(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.code);
        codeNumber = typedArray.getInteger(R.styleable.code_codeNumber, 6);
        codeSpace = typedArray.getInteger(R.styleable.code_codeSpace, 20);
        lengthSide = typedArray.getInteger(R.styleable.code_lengthSide, 50);
        textSize = typedArray.getFloat(R.styleable.code_textSize, 20);
        inputType = typedArray.getInteger(R.styleable.code_inputType, 2);
        mEditparams = new LinearLayout.LayoutParams(lengthSide, lengthSide);
        mViewparams = new LinearLayout.LayoutParams(codeSpace, lengthSide);
        initView();
    }

    /**
     * 初始化输入框
     */
    private void initView() {
        for (int i = 0; i < codeNumber; i++) {
            EditText editText = new EditText(mContext);
            editText.setCursorVisible(false);
            editText.setOnKeyListener(this);
            editText.setTextSize(textSize);
            editText.setInputType(inputType);
            editText.setTextColor(textColor);
            editText.setPadding(0, 0, 0, 0);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            editText.addTextChangedListener(this);
            editText.setLayoutParams(mEditparams);
            editText.setGravity(Gravity.CENTER);
            editText.setBackgroundResource(R.drawable.shape_edit);
            addView(editText);
            mEditTextList.add(editText);
            if (i != codeNumber - 1) {
                View view = new View(mContext);
                view.setLayoutParams(mViewparams);
                addView(view);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (i == 0 && i2 >= 1 && currentPosition != mEditTextList.size() - 1) {
            currentPosition++;
            mEditTextList.get(currentPosition).requestFocus();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * 监听删除键
     * @param view
     * @param i
     * @param keyEvent
     * @return
     */
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        EditText editText = (EditText) view;
        if (i == KeyEvent.KEYCODE_DEL && editText.getText().length() == 0) {
            int action = keyEvent.getAction();
            if (currentPosition != 0 && action == KeyEvent.ACTION_DOWN) {
                currentPosition--;
                mEditTextList.get(currentPosition).requestFocus();
                mEditTextList.get(currentPosition).setText("");
            }
        }
        return false;
    }

    /**
     * 获取验证码
     * @return
     */
    public String getVerificationCode() {
        StringBuffer stringBuffer = new StringBuffer();
        for (EditText string : mEditTextList) {
            stringBuffer.append(string.getText().toString());
        }
        return stringBuffer.toString();
    }

}
