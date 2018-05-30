package com.gsy.ml.ui.common;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ml.gsy.com.library.common.LoadingDialog;


/**
 * Fragment's 基类
 * <p>
 * Created by xw on 2016-8-3.
 */
public class BaseFragment extends Fragment implements
        I_WaitDialog {
    /**
     * Fragment根视图
     */
    protected View mFragmentRootView;

    /**
     * 加载进度
     */
    private LoadingDialog mLoadingDialog;

    /**
     * 当前ViewDataBinding
     */
    public ViewDataBinding vdb;
    /**
     * 当前Fragment所在Activity
     */
    public Activity aty;

    /**
     * 加载View
     *
     * @param inflater  LayoutInflater
     * @param container ViewGroup
     * @param bundle    Bundle
     * @return
     */
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        vdb = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return vdb.getRoot();
    }

    /**
     * 获取布局文件，一般情况直接重写改方法设置布局文件
     *
     * @return
     */
    public int getLayoutId() {
        return 0;
    }

    /**
     * 初始化控件
     *
     * @param parentView 根View
     */
    protected void initWidget(View parentView) {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 初始化，包含initData  initWidget
     */
    public void initializer(View parentView) {
        initData();
        initWidget(parentView);
    }

    /**
     * 当通过changeFragment()显示时会被调用(类似于onResume)
     */
    public void onChange() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentRootView = inflaterView(inflater, container, savedInstanceState);
        aty = getActivity();


        initializer(mFragmentRootView);
        return mFragmentRootView;
    }

    public void showToast(String str) {
        Toast.makeText(aty, str, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //  ButterKnife.reset(this);
        //
        aty = null;
    }


    /***************************************************************************
     * 弹出窗方法
     ***************************************************************************/
    /**
     * 隐藏
     */
    @Override
    public void hideWaitDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    /**
     * 显示（默认不可取消）
     *
     * @param message 消息
     * @return
     */
    @Override
    public LoadingDialog showWaitDialog(String message) {
        return showWaitDialog(message, false, null);
    }

    @Override
    public LoadingDialog showWaitDialog(boolean isCancel, DialogInterface.OnCancelListener cancelListener) {
        return showWaitDialog("", isCancel, cancelListener);
    }


    /**
     * 显示（默认不可取消）
     *
     * @return
     */
    @Override
    public LoadingDialog showWaitDialog() {
        return showWaitDialog("", false, null);
    }

    /**
     * 显示
     *
     * @param message        消息
     * @param isCancel       是否可取消
     * @param cancelListener 取消监听
     * @return
     */
    @Override
    public LoadingDialog showWaitDialog(String message, boolean isCancel, DialogInterface.OnCancelListener cancelListener) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this.getActivity(), message);
        }

        mLoadingDialog.setCancelable(isCancel);
        if (isCancel == true && cancelListener != null) {
            mLoadingDialog.setOnCancelListener(cancelListener);
        }

        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }

        return mLoadingDialog;
    }

}
