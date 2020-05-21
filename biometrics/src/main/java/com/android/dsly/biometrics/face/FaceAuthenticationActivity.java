package com.android.dsly.biometrics.face;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.dsly.biometrics.R;
import com.android.dsly.biometrics.databinding.ActivityFaceAuthenticationBinding;
import com.android.dsly.biometrics.face.exception.FaceError;
import com.android.dsly.biometrics.face.model.RegResult;
import com.android.dsly.biometrics.face.utils.Md5;
import com.android.dsly.biometrics.face.utils.OnResultListener;
import com.blankj.utilcode.util.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class FaceAuthenticationActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityFaceAuthenticationBinding mBinding;

    private static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_face_authentication);

        addListener();
    }

    private void addListener() {
        mBinding.nextBtn.setOnClickListener(this);
        mBinding.backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next_btn:
                String username = mBinding.usernameEt.getText().toString().trim();
                // uid应使用你系统的用户id，示例里暂时用用户名
                String uid = username;
                if (TextUtils.isEmpty(uid)) {
                    Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(this, FaceScanActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.back_btn:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE) {
            // filePath = data.getStringExtra("file_path");
            String filePath = getExternalCacheDir().getAbsolutePath()+"/face/face1.png";
            faceLogin(filePath);
        }
    }

    /**
     * 上传图片进行比对，分数达到80认为是同一个人，认为登录可以通过
     * 建议上传自己的服务器，在服务器端调用https://aip.baidubce.com/rest/2.0/face/v3/search，比对分数阀值（如：80分），认为登录通过
     * 返回登录认证的参数给客户端
     *
     * @param filePath
     */
    private void faceLogin(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            ToastUtils.showLong("人脸图片不存在");
            return;
        }

        final String username = mBinding.usernameEt.getText().toString().trim();
        // uid应使用你系统的用户id，示例里暂时用用户名
        String uid = Md5.MD5(username, "utf-8");

        final File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(this, "人脸图片不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        mBinding.inputLl.setVisibility(View.GONE);
        APIService.getInstance().verify(new OnResultListener<RegResult>() {
            @Override
            public void onResult(RegResult result) {
                // deleteFace(file);
                mBinding.inputLl.setVisibility(View.VISIBLE);
                if (result == null) {
                    return;
                }

                displayData(result);
            }

            @Override
            public void onError(FaceError error) {
                error.printStackTrace();
                mBinding.inputLl.setVisibility(View.VISIBLE);
                displayError(error);
            }
        }, file, uid);
    }

    private void displayData(RegResult result) {
        String res = result.getJsonRes();
        Log.d("VerifyLoginActivity", "res is:" + res);
        double maxScore = 0;
        String userId = "";
        String userInfo = "";
        if (TextUtils.isEmpty(res)) {
            return;
        }

        mBinding.inputLl.setVisibility(View.GONE);
        mBinding.resultLl.setVisibility(View.VISIBLE);

        JSONObject obj = null;
        try {
            obj = new JSONObject(res);
            JSONObject resObj = obj.optJSONObject("result");
            if (resObj != null) {
                JSONArray resArray = resObj.optJSONArray("user_list");
                int size = resArray.length();

                for (int i = 0; i < size; i++) {
                    JSONObject s = (JSONObject) resArray.get(i);
                    if (s != null) {
                        double score = s.getDouble("score");
                        if (score > maxScore) {
                            maxScore = score;
                            userId = s.getString("user_id");
                            userInfo = s.getString("user_info");
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (maxScore < 80) {
            mBinding.resultTv.setText("识别失败");
            mBinding.scoreTv.setText("人脸识别分数过低：" + maxScore);
        } else {
            // 分数可以根据安全级别调整，
            mBinding.resultTv.setText("识别成功");
            if (!TextUtils.isEmpty(userInfo)) {
                mBinding.uidTv.setText(userInfo);
            } else {
                mBinding.uidTv.setText(userId);
            }
            mBinding.scoreTv.setText("人脸识别分数:" + maxScore);
        }
    }

    private void displayError(FaceError error) {
        mBinding.inputLl.setVisibility(View.GONE);
        mBinding.resultLl.setVisibility(View.VISIBLE);
        mBinding.resultTv.setText("识别失败");
        mBinding.scoreTv.setText(error.getErrorMessage());
    }
}
