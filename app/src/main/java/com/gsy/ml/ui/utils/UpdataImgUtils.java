package com.gsy.ml.ui.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.gsy.ml.common.Link;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络请求Api
 *
 * @author Wanwan
 * @date 15/8/19
 */
public class UpdataImgUtils {

    UpdataImgCallBack callBack;

    Context mContext;
    OSS mOSS;
    boolean isSuccess = true;


    public UpdataImgUtils(UpdataImgCallBack callBack, Context mContext) {
        this.callBack = callBack;
        this.mContext = mContext;
        initOSS();
    }


    public void cancelUpdata() {
        this.isSuccess = false;
        for (OSSAsyncTask task : taskList) {
            if (task != null) {
                task.cancel();
            }
        }
    }

    private void initOSS() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Link.accessKeyId, Link.accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(5 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(10 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(15); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        mOSS = new OSSClient(mContext, Link.endpoint, credentialProvider, conf);
    }

    /**
     * bucketName ： OSS上的文件夹名
     * objectKey ： OSS上的文件名
     * uploadFilePath ： 文件在本地的路径
     */

    List<OSSAsyncTask> taskList = new ArrayList<>();

    public void updataImgs(final String bucketName, final List<String> objectKey, final List<String> uploadFilePath) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                List<PutObjectRequest> putList = (List<PutObjectRequest>) msg.obj;
                if (putList != null && putList.size() > 0) {
                    for (int i = 0; i < putList.size(); i++) {
                        OSSAsyncTask task = mOSS.asyncPutObject(putList.get(i), new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                            @Override
                            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                                callBack.onUpdataImgResult(request.getObjectKey(), true);
                            }

                            @Override
                            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                                // 请求异常
                                if (clientExcepion != null) {
                                    // 本地异常如网络异常等
                                    clientExcepion.printStackTrace();
                                    Log.e("clientExcepion", clientExcepion.getMessage());
                                }
                                if (serviceException != null) {
                                    Log.e("ErrorCode", serviceException.getErrorCode());
                                    Log.e("RequestId", serviceException.getRequestId());
                                    Log.e("HostId", serviceException.getHostId());
                                    Log.e("RawMessage", serviceException.getRawMessage());
                                }
                                callBack.onUpdataImgResult(request.getObjectKey(), false);
                            }
                        });
                    }
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<PutObjectRequest> putList = new ArrayList<PutObjectRequest>();
                for (int i = 0; i < uploadFilePath.size(); i++) {
                    PutObjectRequest put;
                    if (uploadFilePath.get(i).contains(".mp4") || uploadFilePath.get(i).contains(".gif")) {
                        put = new PutObjectRequest(bucketName, objectKey.get(i), uploadFilePath.get(i));
                    } else {
                        byte[] datares = ImageUtils.compressLimit(uploadFilePath.get(i));//压缩文件
                        int length = datares.length;
                        put = datares == null ? new PutObjectRequest(bucketName, objectKey.get(i), uploadFilePath.get(i)) : new PutObjectRequest(bucketName, objectKey.get(i), datares);

                    }
                    putList.add(put);
                }
                Message msg = new Message();
                msg.obj = putList;
                handler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * bucketName ： OSS上的文件夹名
     * objectKey ： OSS上的文件名
     * uploadFilePath ： 文件在本地的路径
     */
    public void updataImg(String bucketName, String objectKey, String uploadFilePath) {
        PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, uploadFilePath);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = mOSS.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                callBack.onUpdataImgResult(request.getObjectKey(), true);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    Log.e("clientExcepion", clientExcepion.getMessage());
                }
                if (serviceException != null) {
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
                callBack.onUpdataImgResult(request.getObjectKey(), false);
            }
        });

    }

    public interface UpdataImgCallBack {
        void onUpdataImgResult(String key, boolean state);
    }

}
