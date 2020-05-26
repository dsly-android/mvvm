package com.htxtdshopping.htxtd.frame.ui.second.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.utils.ToastUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityLoginAndShareBinding;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.lang.ref.WeakReference;
import java.util.Map;

public class LoginAndShareActivity extends BaseFitsWindowActivity<ActivityLoginAndShareBinding, BaseViewModel> implements View.OnClickListener {

    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login_and_share;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mShareListener = new CustomShareListener(this);
        UMWeb web = new UMWeb("https://www.baidu.com");
        web.setTitle("This is web title");
        web.setThumb(new UMImage(this, "网络图片地址"));
        web.setDescription("my description");
        mShareAction = new ShareAction(this)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(mShareListener);

        changeUI();
    }

    private void changeUI() {
        //判断微信是否授权
        boolean isauth = UMShareAPI.get(this).isAuthorize(this, SHARE_MEDIA.WEIXIN);
        if (isauth) {
            mBinding.btnWechatLogin.setText("微信删除授权");
        } else {
            mBinding.btnWechatLogin.setText("微信授权");
        }
    }

    @Override
    public void initEvent() {
        mBinding.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_share_ui:
                mShareAction.open();
                break;
            case R.id.btn_wechat_login:
                if (!UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.WEIXIN)) {
                    return;
                }
                boolean isAuth = UMShareAPI.get(this).isAuthorize(this, SHARE_MEDIA.WEIXIN);
                if (isAuth) {
                    UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.WEIXIN, authListener);
                } else {
                    UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.WEIXIN, authListener);
                }
                break;
            case R.id.btn_share_no_ui:
                if (!UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.WEIXIN_CIRCLE)) {
                    return;
                }
                UMWeb web = new UMWeb("https://www.baidu.com");
                web.setTitle("This is web title");
                web.setThumb(new UMImage(this, "网络图片地址"));
                web.setDescription("my description");
                new ShareAction(this).withMedia(web)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(mShareListener).share();
                break;
            default:
                break;
        }
    }

    private UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            ToastUtils.showLong("成功了");
            changeUI();
            StringBuilder sb = new StringBuilder();
            for (String key : data.keySet()) {
                sb.append(key).append(" : ").append(data.get(key)).append("\n");
            }
            mBinding.tvMsg.setText(sb.toString());
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            ToastUtils.showLong("失败：" + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            ToastUtils.showLong("取消了");
        }
    };

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<LoginAndShareActivity> mActivity;

        private CustomShareListener(LoginAndShareActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtils.showLong("分享成功啦");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.showLong("分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showLong("分享取消了");
        }
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
