package com.htxtdshopping.htxtd.frame.ui.second.activity;

import android.os.Bundle;
import android.view.View;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.android.dsly.common.base.BaseActivity;
import com.blankj.utilcode.util.FileUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.network.OssService;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * @author chenzhipeng
 */
public class OssActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_oss;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_upload_single, R.id.btn_upload_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_upload_single:
                String path = "本地路径";
                OssService.getInstance().asyncPutObject(path, null, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //远程路径
                                String ImgURl = OssService.getInstance().generateUrl(request.getObjectKey());
                            }
                        });
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {

                    }
                });
                break;
            case R.id.btn_upload_more:
                //本地路径
                List<String> filePaths = new ArrayList<>();
                OssService.getInstance().asyncPutObjects(filePaths, new OssService.OnUploadFileListener() {
                    @Override
                    public void onFinish(List<String> localPaths, List<String> uploadPaths) {
                        for (int i = 0; i < localPaths.size(); i++) {
                            FileUtils.deleteFile(localPaths.get(i));
                        }
                        /*runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < uploadPaths.size(); i++) {
                                    addData((String) uploadPaths.get(i));
                                }
                            }
                        });*/
                    }
                });
                break;
            default:
                break;
        }
    }
}
