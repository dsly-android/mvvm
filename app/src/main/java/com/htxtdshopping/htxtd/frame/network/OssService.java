package com.htxtdshopping.htxtd.frame.network;

import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.android.dsly.common.constant.Constants;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.Utils;
import com.htxtdshopping.htxtd.frame.utils.AppSPUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * @author 陈志鹏
 * @date 2018/7/30
 */
public class OssService {

    //永久保存
    private static final String DIR_SAVE_FOREVER = "save_forever";
    //可以删除
    private static final String DIR_CAN_DELETE = "can_delete";

    private OSS oss;
    private String bucket;
    private static OssService ossService;

    public static void init(String bucket) {
        if (ossService == null) {
            ossService = new OssService(bucket);
        }
    }

    public static OssService getInstance() {
        return ossService;
    }

    private OssService(String bucket) {
        this.bucket = bucket;
        initOss();
    }

    private void initOss() {
        //OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider
        // (Constants.OSS_ACCESSKEYID, Constants.OSS_ACCESSKEYSECRET);
        //使用自己的获取STSToken的类
        OSSCredentialProvider credentialProvider = new STSProvider();

        ClientConfiguration conf = new ClientConfiguration();
        // 连接超时，默认15秒
        conf.setConnectionTimeout(15 * 1000);
        // socket超时，默认15秒
        conf.setSocketTimeout(15 * 1000);
        // 最大并发请求数，默认5个
        conf.setMaxConcurrentRequest(5);
        // 失败后最大重试次数，默认2次
        conf.setMaxErrorRetry(2);

        ThreadUtils.executeByIo(new ThreadUtils.SimpleTask<OSS>() {
            @Override
            public OSS doInBackground() throws Throwable {
                OSS oss = new OSSClient(Utils.getApp(), Constants.OSS_ENDPOINT, credentialProvider, conf);
                return oss;
            }

            @Override
            public void onSuccess(OSS oss) {
                ossService.oss = oss;
            }
        });
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

    public PutObjectResult putByteData(String dir, byte[] datas) {
        if (datas == null) {
            return null;
        }

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucket, generateKey(dir, null), datas);
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
    public PutObjectResult putObject(String dir, String localFile) {
        File file = new File(localFile);
        if (!file.exists()) {
            return null;
        }
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucket, generateKey(dir, localFile), localFile);
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

    public OSSAsyncTask asyncPutHeadImg(String localFile,
                                        final OSSProgressCallback<PutObjectRequest> userProgressCallback,
                                        @NonNull final OSSCompletedCallback<PutObjectRequest, PutObjectResult> userCallback) {
        return asyncPutObject(DIR_SAVE_FOREVER + "/headImg", localFile, userProgressCallback, userCallback);
    }

    /**
     *
     * @param dir oss上保存文件的目录
     */
    public OSSAsyncTask asyncPutObject(String dir, String localFile,
                                       final OSSProgressCallback<PutObjectRequest> userProgressCallback,
                                       @NonNull final OSSCompletedCallback<PutObjectRequest, PutObjectResult> userCallback) {
        File file = new File(localFile);
        if (!file.exists()) {
            return null;
        }

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucket, generateKey(dir, localFile), localFile);

        // 异步上传时可以设置进度回调
        if (userProgressCallback != null) {
            put.setProgressCallback(userProgressCallback);
        }

        OSSAsyncTask task = oss.asyncPutObject(put, userCallback);
        return task;
    }

    public List<OSSAsyncTask> asyncPutObjects(String dir, List<String> localFiles, OnUploadFileListener listener) {
        List<OSSAsyncTask> tasks = new ArrayList<>();
        HashMap<String, String> netPaths = new HashMap<>();
        final int[] count = {0};
        for (int i = 0; i < localFiles.size(); i++) {
            OSSAsyncTask task = asyncPutObject(dir, localFiles.get(i), null, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                    String uploadFilePath = request.getUploadFilePath();
                    netPaths.put(uploadFilePath, generateUrl(request.getObjectKey()));
                    count[0]++;
                    if (count[0] >= localFiles.size()) {
                        listener.onFinish(localFiles, netPaths);
                    }
                }

                @Override
                public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                    count[0]++;
                    if (count[0] >= localFiles.size()) {
                        listener.onFinish(localFiles, netPaths);
                    }
                }
            });
            tasks.add(task);
        }
        return tasks;
    }

    public interface OnUploadFileListener {
        void onFinish(List<String> localPaths, HashMap<String, String> netPaths);
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
    private String generateKey(String dir, String fileName) {
        long userId = AppSPUtils.getUserId();
        long currentTimeMillis = System.currentTimeMillis();
        if (mOldTimestamp == currentTimeMillis) {
            mDigital++;
        } else {
            mOldTimestamp = currentTimeMillis;
            mDigital = 0;
        }
        return dir + "/" + userId + mOldTimestamp + mDigital + "." + (TextUtils.isEmpty(fileName) ? "" : fileName.substring(fileName.lastIndexOf(".") + 1));
    }
}