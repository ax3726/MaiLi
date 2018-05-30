package com.gsy.ml.ui.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.gsy.ml.model.main.JingWeiModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/6/27.
 */

public class SocaketUtils {
    private static final String TAG = "SocaketUtils";
    /**
     * 心跳检测时间
     */
    private static final long HEART_BEAT_RATE = 3 * 1000;
    /**
     * 主机IP地址
     */
    // private static final String HOST = "192.168.1.188";
    private static final String HOST = "211.155.232.67";
    /**
     * 端口号
     */
    public static final int PORT = 9001;


    private long sendTime = 0L;
    /**
     * 心跳包内容
     */
    private static final String XINTIAO = "xintiao";


    /**
     * 弱引用 在引用对象的同时允许对垃圾对象进行回收
     */
    private WeakReference<Socket> mSocket;

    private ReadThread mReadThread;
    private InitSocketThread mInitSocketThread;

    private static SocaketUtils mSocaketUtils;
    private static SocaketUtils mSocaketUtils1;

    private boolean isRunning = false;//正在运行

    public static SocaketUtils getInstance() {

        if (mSocaketUtils == null) {
            mSocaketUtils = new SocaketUtils();
        }
        return mSocaketUtils;
    }

    public static SocaketUtils getInstance1() {

        if (mSocaketUtils1 == null) {
            mSocaketUtils1 = new SocaketUtils();
        }
        return mSocaketUtils1;
    }

    private SocaketUtils() {

    }

    /**
     * 开始连接
     */
    public void startConnection() {
        mInitSocketThread = new InitSocketThread();
        mInitSocketThread.start();
        isRunning = true;
    }

    public void CloseScoket() {
        isRunning = false;
        if (mReadThread != null) {
            //  mHandler.removeCallbacks(heartBeatRunnable);
            mReadThread.release();
            mReadThread = null;
            mInitSocketThread = null;
            mSocaketUtils = null;
        }
    }

    // 发送心跳包
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final String obj = (String) msg.obj;
            if (XINTIAO.equals(obj)) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendMsg(obj);
                    }
                }, 5000);

            } else if (!TextUtils.isEmpty(obj) && obj.contains("\"status\":")) {
                if (mISocaketListener != null) {
                    JingWeiModel modle = null;
                    try {
                        modle = ParseJsonUtils.getBean((String) obj, JingWeiModel.class);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    if (modle != null) {
                        mISocaketListener.onLocation(modle);
                    }
                }
            } else {
                // Toast.makeText(MaiLiApplication.appliactionContext, obj, Toast.LENGTH_SHORT).show();
            }
        }
    };


    /* private Runnable heartBeatRunnable = new Runnable() {
         @Override
         public void run() {
             if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {

                 boolean isSuccess = sendMsg(XINTIAO);// 就发送一个\r\n过去, 如果发送失败，就重新初始化一个socket
                 if (!isSuccess) {
                     Message me = new Message();
                     me.obj = "连接失败,正在重启";
                     mHandler.sendMessage(me);
                     mHandler.removeCallbacks(heartBeatRunnable);
                     mReadThread.release();
                     releaseLastSocket(mSocket);
                     mInitSocketThread = null;
                     mInitSocketThread = new InitSocketThread();
                     mInitSocketThread.start();
                 } else {
                     Message me = new Message();
                     me.obj = "连接成功";
                     mHandler.sendMessage(me);
                     if (mISocaketListener != null) {
                         mISocaketListener.onSuccess();
                     }
                 }
             }
             mHandler.postDelayed(this, HEART_BEAT_RATE);
         }
     };*/
    private void reStart() {
        mHandler.postDelayed(new Runnable() {//3秒后重启
            @Override
            public void run() {
                if (mReadThread != null) {
                    mReadThread.release();
                }
                mInitSocketThread = null;
                mInitSocketThread = new InitSocketThread();
                mInitSocketThread.start();
            }
        }, 3000);
//                mHandler.removeCallbacks(heartBeatRunnable);

    }

    public boolean sendMsg(String msg) {
        if (!isRunning) {
            return false;
        }
        if (null == mSocket || null == mSocket.get()) {
         /*   Message me = new Message();
            me.obj = "连接为空,正在尝试重启";
            mHandler.sendMessage(me);*/
            reStart();
            return false;
        }
        Socket soc = mSocket.get();

        try {
            if (!soc.isClosed() && !soc.isOutputShutdown()) {
                OutputStream os = soc.getOutputStream();
                String message = msg + "\r\n";
                os.write(message.getBytes());
                os.flush();

                sendTime = System.currentTimeMillis();// 每次发送成功数据，就改一下最后成功发送的时间，节省心跳间隔时间
                // Log.e(TAG, "发送成功的时间：" + sendTime);
            } else {//连接断开
              /*  Message me = new Message();
                me.obj = "连接断开,正在尝试重启";
                mHandler.sendMessage(me);*/
                reStart();
                return false;
            }
        } catch (Exception e) {
            if (!"android.os.NetworkOnMainThreadException".equals(e.toString().trim())) {
                e.printStackTrace();
             /*   Message me = new Message();
                me.obj = e.toString();
                mHandler.sendMessage(me);*/
                reStart();
            }
            return false;
        }
        return true;
    }

    // 初始化socket
    private void initSocket() throws UnknownHostException, IOException {
        Log.e(TAG, "开始：");
        Socket socket = new Socket(HOST, PORT);
        mSocket = new WeakReference<Socket>(socket);
        mReadThread = new ReadThread(socket);
        mReadThread.start();
        Log.e(TAG, "开始1：");

        boolean isSuccess = sendMsg(XINTIAO);
        if (isSuccess) {//连接成功
            if (mISocaketListener != null) {
                mISocaketListener.onSuccess();
            }
        }


        //   mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);// 初始化成功后，就准备发送心跳包
    }

    // 释放socket
    private void releaseLastSocket(WeakReference<Socket> mSocket) {
        try {
            if (null != mSocket) {
                Socket sk = mSocket.get();
                if (!sk.isClosed()) {
                    sk.close();
                }
                sk = null;
                mSocket = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                initSocket();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class ReadThread extends Thread {
        private WeakReference<Socket> mWeakSocket;
        private boolean isStart = true;

        public ReadThread(Socket socket) {
            mWeakSocket = new WeakReference<Socket>(socket);
        }

        public void release() {
            isStart = false;
            releaseLastSocket(mWeakSocket);
        }

        @SuppressLint("NewApi")
        @Override
        public void run() {
            super.run();
            Socket socket = mWeakSocket.get();
            if (null != socket) {
                try {
                    InputStream is = socket.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int length = 0;
                    while (!socket.isClosed() && !socket.isInputShutdown()
                            && isStart && ((length = is.read(buffer)) != -1)) {
                        if (length > 0) {
                            String message = new String(Arrays.copyOf(buffer,
                                    length)).trim();
                            //  Log.e(TAG, "收到服务器发送来的消息：" + message);
                            Message me = new Message();
                            me.obj = message;

                            mHandler.sendMessage(me);

                            // 收到服务器过来的消息，就通过Broadcast发送出去

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ISocaketListener mISocaketListener;

    public void setISocaketListener(ISocaketListener mISocaketListener) {
        this.mISocaketListener = mISocaketListener;
    }

    public interface ISocaketListener {
        void onSuccess();

        void onLocation(JingWeiModel jingWeiModel);
    }

}
