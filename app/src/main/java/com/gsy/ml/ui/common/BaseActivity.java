package com.gsy.ml.ui.common;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;

import ml.gsy.com.library.common.LoadingDialog;

import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
import static android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;


/**
 * Created by Administrator on 2016/11/14 0014.
 */
public class BaseActivity extends FragmentActivity implements I_BaseActivity, I_WaitDialog {
    private static final String TAG = "BaseActivity";
    /**
     * 加载进度
     */
    private LoadingDialog mLoadingDialog;
    /**
     * 当前ViewDataBinding
     */
    public ViewDataBinding vdb;

    /**
     * 当前Activity
     */
    public Activity aty;
    /**
     * Activity状态
     */
    public int activityState = DESTROY;
    public Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

       /* FLAG_DISMISS_KEYGUARD：使用这个flag时，系统会自动解锁屏幕。
        FLAG_TURN_SCREEN_ON：当window被显示的时候，系统把FLAG_TURN_SCREEN_ON当做一个用户活动事件，用以点亮屏幕。
        FLAG_KEEP_SCREEN_ON：当window对用户可见的时候，系统让屏幕处于高亮状态。*/
        getWindow().addFlags(
                FLAG_SHOW_WHEN_LOCKED |
                        FLAG_DISMISS_KEYGUARD |
                        FLAG_TURN_SCREEN_ON |
                        FLAG_KEEP_SCREEN_ON);

        setRootView();
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/
        aty = this;
        //try {
        initializer();
      /* } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MaiLiApplication.appliactionContext, e.toString(), Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("yichang", "被回收掉了!");

    }

    /**
     * 打开新的Activity
     *
     * @param intent intent
     * @param isAnim 是否开启过渡动画
     */
    public void startActivity(Intent intent, boolean isAnim) {
        if (isAnim) {
            startActivity(intent, R.anim.act_right_in, R.anim.act_left_out);
        } else {
            super.startActivity(intent);
        }
    }

    /**
     * 打开新Activity
     *
     * @param intent  intent
     * @param animIn  新Activity进入的动画
     * @param animOut 当前Activity退出的动画
     */
    public void startActivity(Intent intent, int animIn, int animOut) {
        super.startActivity(intent);
        overridePendingTransition(animIn, animOut);
    }

    /**
     * 退出Activity
     *
     * @param isAnim 是否开启过渡动画
     */
    public void finish(boolean isAnim) {
        if (isAnim) {
            finish(R.anim.act_left_in, R.anim.act_right_out);
        } else {
            super.finish();
        }
    }

    /**
     * 退出Activity
     *
     * @param animIn  老Activity进入的动画
     * @param animOut 当前Activity退出的动画
     */
    public void finish(int animIn, int animOut) {
        super.finish();
        overridePendingTransition(animIn, animOut);
    }


    @Override
    public void setRootView() {
        vdb = DataBindingUtil.setContentView(this, getLayoutId());
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initActionBar() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initWidget() {

    }

    /**
     * 初始化，包含initData  initActionBar  initWidget
     */
    public void initializer() {
        initData();
        initActionBar();
        initWidget();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        activityState = RESUME;

    }

    @Override
    protected void onPause() {
        super.onPause();
        activityState = PAUSE;

    }

    @Override
    protected void onStop() {
        super.onStop();
        activityState = STOP;

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public void showToast(String str) {
        Toast.makeText(aty, str, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        activityState = DESTROY;
        if (MaiLiApplication.getInstance().mInformationDialog != null) {
            if (MaiLiApplication.getInstance().mInformationDialog.isShowing()) {
                MaiLiApplication.getInstance().mInformationDialog.dismiss();
            }
        }
        if (MaiLiApplication.getInstance().mOrdersDialog != null) {
            if (MaiLiApplication.getInstance().mOrdersDialog.isShowing()) {
                MaiLiApplication.getInstance().mOrdersDialog.dismiss();
            }
        }
        super.onDestroy();
        //取消Http任务  可销毁的在Sign后面增加标记
        aty = null;
    }

    /**
     * 显示（默认不可取消）
     *
     * @param message 消息
     * @return
     */

    public LoadingDialog showWaitDialog(String message) {
        return showWaitDialog(message, true, null);
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

    public LoadingDialog showWaitDialog() {
        return showWaitDialog("", true, null);
    }

    /**
     * 显示
     *
     * @param message        消息
     * @param isCancel       是否可取消
     * @param cancelListener 取消监听
     * @return
     */

    public LoadingDialog showWaitDialog(String message, boolean isCancel, DialogInterface.OnCancelListener cancelListener) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, message);
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
    /***************************************************************************
     * 弹出窗方法
     ***************************************************************************/
    /**
     * 隐藏
     */
    public void hideWaitDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }


}
