package com.hyphenate.easeui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.easeui.R;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;
import ml.gsy.com.library.utils.CacheUtils;

/**
 * Created by Administrator on 2017/9/8.
 */

public class CommonLanguage extends LinearLayout implements View.OnClickListener {
    private LinearLayout llyClick;

    private RecyclerView rvMeassage;
    private CommonAdapter<String> adapter;
    private List<String> list = new ArrayList<>();
    private UsefulExpressionsDialog mUsefulExpressionsDialog;

    private LinearLayout llyFunction;
    private TextView tvAdd;
    private boolean isEdit = false;//是否编辑
    private Context context;
    protected LayoutInflater layoutInflater;
    private ImageView imgSetting;
    private final static String COMLANGUAGE = "common_language";//常用语

    public CommonLanguage(Context context) {
        super(context);
        init(context);
    }

    public CommonLanguage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonLanguage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void loadData() {
        List<String> list = (List<String>) CacheUtils.getInstance().loadCache(COMLANGUAGE);
        if (list != null) {
            this.list.clear();
            this.list.addAll(list);
        }

    }

    private void saveData() {
        CacheUtils.getInstance().saveCache(COMLANGUAGE, this.list);
    }

    private void init(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.common_language_layout, this);
        mUsefulExpressionsDialog = new UsefulExpressionsDialog(context);
        llyClick = (LinearLayout) findViewById(R.id.lly_click);
        imgSetting = (ImageView) findViewById(R.id.img_setting);

        rvMeassage = (RecyclerView) findViewById(R.id.rv_meassage);
        tvAdd = (TextView) findViewById(R.id.tv_add);
        llyClick.setOnClickListener(this);
        imgSetting.setOnClickListener(this);
        loadData();

        mUsefulExpressionsDialog.setUsefulExpressionsListener(new UsefulExpressionsDialog.UsefulExpressionsListener() {
            @Override
            public void setItem(int type, String text) {
                if (type == 0) {
                    list.add(text);
                    adapter.notifyDataSetChanged();
                    saveData();
                } else if (type == 1)//发送
                {
                    if (mCommonLanguageListener != null) {
                        mCommonLanguageListener.onItemClick(text);
                    }
                    list.add(text);
                    adapter.notifyDataSetChanged();
                    saveData();
                }
            }

            @Override
            public void editItem(int type, int position, String text) {
                if (type == 1)//发送
                {
                    if (mCommonLanguageListener != null) {
                        mCommonLanguageListener.onItemClick(text);
                    }
                }
                list.set(position, text);
                adapter.notifyDataSetChanged();
            }
        });
        setAdapters();
    }

    public void setAdapters() {
        adapter = new CommonAdapter<String>(context, R.layout.item_meassage, list) {
            @Override
            protected void convert(ViewHolder holder, final String s, final int position) {
                TextView textView = holder.getView(R.id.tv_content);
                ImageView img_edit = holder.getView(R.id.img_edit);
                ImageView img_del = holder.getView(R.id.img_del);
                LinearLayout llyFunction = holder.getView(R.id.lly_function);
                if (isEdit) {
                    llyFunction.setVisibility(View.VISIBLE);
                    img_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mUsefulExpressionsDialog.show();
                            mUsefulExpressionsDialog.setText(s, position);
                        }
                    });
                    img_del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            list.remove(position);
                            adapter.notifyDataSetChanged();

                        }
                    });
                } else {
                    llyFunction.setVisibility(View.GONE);
                }

                textView.setText(s);
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCommonLanguageListener != null) {
                            mCommonLanguageListener.onItemClick(s);
                        }
                    }
                });
            }
        };
        rvMeassage.setLayoutManager(new LinearLayoutManager(context));
        rvMeassage.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.lly_click) {
            if ("完成".equals(tvAdd.getText().toString().trim())) {
                isEdit = false;
                imgSetting.setVisibility(View.VISIBLE);
                tvAdd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.common_add, 0, 0, 0);
                tvAdd.setText("新增");
                adapter.notifyDataSetChanged();
                saveData();
            } else {
                mUsefulExpressionsDialog.show();
            }
        } else if (i == R.id.img_setting) {
            isEdit = true;
            imgSetting.setVisibility(View.GONE);
            tvAdd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvAdd.setText("完成");
            adapter.notifyDataSetChanged();
        }
    }

    private CommonLanguageListener mCommonLanguageListener;

    public void setCommonLanguageListener(CommonLanguageListener mCommonLanguageListener) {
        this.mCommonLanguageListener = mCommonLanguageListener;
    }

    public interface CommonLanguageListener {
        void onItemClick(String item);

    }

}
