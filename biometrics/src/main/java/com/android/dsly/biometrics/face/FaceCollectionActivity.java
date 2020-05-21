package com.android.dsly.biometrics.face;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.dsly.biometrics.R;
import com.android.dsly.biometrics.databinding.ActivityFaceCollectionBinding;
import com.android.dsly.biometrics.face.exception.FaceError;
import com.android.dsly.biometrics.face.model.RegResult;
import com.android.dsly.biometrics.face.utils.Md5;
import com.android.dsly.biometrics.face.utils.OnResultListener;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class FaceCollectionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_DETECT_FACE = 1000;

    private ActivityFaceCollectionBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_face_collection);

        initEvent();
    }

    private void initEvent() {
        mBinding.detectBtn.setOnClickListener(this);
        mBinding.submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!PermissionUtils.isGranted(Manifest.permission.CAMERA)) {
            new RxPermissions(this).request(Manifest.permission.CAMERA);
        }
        switch (v.getId()) {
            case R.id.detect_btn:
                Intent intent = new Intent(this, FaceScanActivity.class);
                startActivityForResult(intent, REQUEST_CODE_DETECT_FACE);
                break;
            case R.id.submit_btn:
                if (TextUtils.isEmpty(facePath)) {
                    ToastUtils.showLong("请先进行人脸采集");
                } else {
                    reg(facePath);
                }
                break;
            default:
                break;
        }
    }

    private String facePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_DETECT_FACE) {
            facePath = getExternalCacheDir().getAbsolutePath()+"/face/face1.png";
            Bitmap headBmp = ImageUtils.getBitmap(facePath);
            if (headBmp != null) {
                mBinding.avatarIv.setImageBitmap(headBmp);
            }
        }
    }

    private void reg(String filePath) {
        String username = mBinding.usernameEt.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.showLong("姓名不能为空");
            return;
        }

//        if (!isELineCharacter(username)) {
//            toast("请输入数字、字母或下划线组合的用户名！");
//            return;
//        }

        final File file = new File(filePath);
        if (!file.exists()) {
            ToastUtils.showLong("文件不存在");
            return;
        }
        // 模拟注册，先提交信息注册获取uid，再使用人脸+uid到百度人脸库注册，

        // 每个开发者账号只能创建一个人脸库；
        // 每个人脸库下，用户组（group）数量没有限制；
        // 每个用户组（group）下，可添加最多300000张人脸，如每个uid注册一张人脸，则最多300000个用户uid；
        // 每个用户（uid）所能注册的最大人脸数量没有限制；
        // 说明：人脸注册完毕后，生效时间最长为35s，之后便可以进行识别或认证操作。
        // 说明：注册的人脸，建议为用户正面人脸。
        // 说明：uid在库中已经存在时，对此uid重复注册时，新注册的图片默认会追加到该uid下，如果手动选择action_type:replace，
        // 则会用新图替换库中该uid下所有图片。
        // uid          是	string	用户id（由数字、字母、下划线组成），长度限制128B
        // user_info    是	string	用户资料，长度限制256B
        // group_id	    是	string	用户组id，标识一组用户（由数字、字母、下划线组成），长度限制128B。
        // 如果需要将一个uid注册到多个group下，group_id,需要用多个逗号分隔，每个group_id长度限制为48个英文字符
        // image	    是	string	图像base64编码，每次仅支持单张图片，图片编码后大小不超过10M
        // action_type	否	string	参数包含append、replace。如果为“replace”，则每次注册时进行替换replace（新增或更新）操作，
        // 默认为append操作
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                faceReg(file);
            }
        }, 1000);
    }

    private void faceReg(File file) {

        // 用户id（由数字、字母、下划线组成），长度限制128B
        // uid为用户的id,百度对uid不做限制和处理，应该与您的帐号系统中的用户id对应。

        //   String uid = UUID.randomUUID().toString().substring(0, 8) + "_123";
        // String uid = 修改为自己用户系统中用户的id;
        // 模拟使用username替代
        String username = mBinding.usernameEt.getText().toString().trim();
        String uid = Md5.MD5(username, "utf-8");


        APIService.getInstance().reg(new OnResultListener<RegResult>() {
            @Override
            public void onResult(RegResult result) {
                Log.i("wtf", "orientation->" + result.getJsonRes());
                ToastUtils.showLong("注册成功！");
                finish();
            }

            @Override
            public void onError(FaceError error) {
                ToastUtils.showLong("注册失败");
            }
        }, file, uid, username);
    }
}
