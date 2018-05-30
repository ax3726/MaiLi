package com.gsy.ml.ui.common;

import android.content.DialogInterface;

import ml.gsy.com.library.common.LoadingDialog;


/**
 * Created by Administrator on 2016/11/14 0014.
 */
public interface I_WaitDialog {

    /**
     * 隐藏WaitDialog
     */
    void hideWaitDialog();

    /**
     * 显示
     *
     * @return
     */
    LoadingDialog showWaitDialog();



    /**
     * 显示
     *
     * @param message 消息
     * @return
     */
    LoadingDialog showWaitDialog(String message);

    /**
     * 显示
     *
     * @param isCancel       是否可取消
     * @param cancelListener 取消监听
     * @return
     */
    LoadingDialog showWaitDialog(boolean isCancel, DialogInterface.OnCancelListener cancelListener);



    /**
     * @param message        消息
     * @param isCancel       是否可取消
     * @param cancelListener 取消监听
     * @return
     */
    LoadingDialog showWaitDialog(String message, boolean isCancel, DialogInterface.OnCancelListener cancelListener);
}
