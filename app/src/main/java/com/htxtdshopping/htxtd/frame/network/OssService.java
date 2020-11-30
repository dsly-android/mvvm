package com.htxtdshopping.htxtd.frame.network;

import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.android.dsly.common.constant.Constants;
import com.blankj.utilcode.util.ObjectUtils;
import com.htxtdshopping.htxtd.frame.utils.AppSPUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * @author 陈志鹏
 * @date 2018/7/30
 */
public class OssService {

    private OSS oss;
    private String bucket;
    private static OssService ossService;

    public static void init(OSS oss, String bucket) {
        if (ossService == null) {
            ossService = new OssService(oss, bucket);
        }
    }

    public static OssService getInstance() {
        return ossService;
    }

    private OssService(OSS oss, String bucket) {
        this.oss = oss;
        this.bucket = bucket;
    }

    /**
     * 同步下载
     */
    public GetObjectResult getObject(String object) {
        if (ObjectUtils.isEmpty(object)) {
            return null;
        }
        GetObjectRequest get = new GetObjectRequest(bucket, object);
        try {
            GetObjectResult result = oss.getObject(get);
            return result;
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步下载
     */
    public OSSAsyncTask asyncGetObject(String object, OSSProgressCallback<GetObjectRequest> progressCallback,
                                       @NonNull OSSCompletedCallback<GetObjectRequest, GetObjectResult> userCallback) {
        if (ObjectUtils.isEmpty(object)) {
            return null;
        }
        GetObjectRequest get = new GetObjectRequest(bucket, object);

        //设置下载进度回调
        if (progressCallback != null) {
            get.setProgressListener(progressCallback);
        }
        OSSAsyncTask task = oss.asyncGetObject(get, userCallback);
        return task;
    }

    public PutObjectResult putByteData(byte[] datas) {
        if (datas == null) {
            return null;
        }

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucket, generateKey(null), datas);
        try {
            PutObjectResult putResult = oss.putObject(put);
            return putResult;
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 同步上传
     */
    public PutObjectResult putObject(String localFile) {
        File file = new File(localFile);
        if (!file.exists()) {
            return null;
        }
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucket, generateKey(localFile), localFile);
        PutObjectResult putResult = null;
        try {
            putResult = oss.putObject(put);
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return putResult;
    }

    public OSSAsyncTask asyncPutObject(String localFile,
                                       final OSSProgressCallback<PutObjectRequest> userProgressCallback,
                                       @NonNull final OSSCompletedCallback<PutObjectRequest, PutObjectResult> userCallback) {
        File file = new File(localFile);
        if (!file.exists()) {
            return null;
        }

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucket, generateKey(localFile), localFile);

        // 异步上传时可以设置进度回调
        if (userProgressCallback != null) {
            put.setProgressCallback(userProgressCallback);
        }

        OSSAsyncTask task = oss.asyncPutObject(put, userCallback);
        return task;
    }

    public List<OSSAsyncTask> asyncPutObjects(List<String> localFiles, OnUploadFileListener listener) {
        List<OSSAsyncTask> tasks = new ArrayList<>();
        List<String> uploadPaths = new ArrayList<>(localFiles);
        final int[] count = {0};
        for (int i = 0; i < localFiles.size(); i++) {
            int finalI = i;
            OSSAsyncTask task = asyncPutObject(localFiles.get(i), null, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                    uploadPaths.set(finalI, generateUrl(request.getObjectKey()));
                    count[0]++;
                    if (count[0] >= localFiles.size()) {
                        listener.onFinish(localFiles, uploadPaths);
                    }
                }

                @Override
                public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                    count[0]++;
                    if (count[0] >= localFiles.size()) {
                        listener.onFinish(localFiles, uploadPaths);
                    }
                }
            });
            tasks.add(task);
        }
        return tasks;
    }

    public interface OnUploadFileListener {
        void onFinish(List<String> localPaths, List<String> uploadPaths);
    }

    /**
     * 生成上传后的url
     */
    public String generateUrl(String key) {
        // http://bucket.<endpoint>/object
//        String path = oss.presignPublicObjectURL(Constants.OSS_BUCKET, key);
        return "http://" + Constants.OSS_BUCKET + "." + Constants.OSS_ENDPOINT.substring(7) + "/" + key;
    }

    private long mOldTimestamp;
    private int mDigital;

    /**
     * 生成objectKey
     */
    private String generateKey(String fileName) {
        long userId = AppSPUtils.getUserId();
        long currentTimeMillis = System.currentTimeMillis();
        if (mOldTimestamp == currentTimeMillis) {
            mDigital++;
        } else {
            mOldTimestamp = currentTimeMillis;
            mDigital = 0;
        }
        return "" + userId + mOldTimestamp + mDigital + "." + (TextUtils.isEmpty(fileName) ? "" : fileName.substring(fileName.lastIndexOf(".") + 1));
    }
}