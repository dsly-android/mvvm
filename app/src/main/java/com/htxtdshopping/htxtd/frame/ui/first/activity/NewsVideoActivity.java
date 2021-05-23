package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityNewsVideoBinding;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import androidx.core.content.ContextCompat;

public class NewsVideoActivity extends BaseActivity<ActivityNewsVideoBinding, BaseViewModel> {

    private LinearLayout mLlTop;
    private boolean isPause;
    private OrientationUtils orientationUtils;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_video;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(android.R.color.transparent));
        BarUtils.setStatusBarLightMode(this, true);

        mLlTop = findViewById(R.id.layout_top);
        mLlTop.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLlTop.getLayoutParams();
        params.topMargin = BarUtils.getStatusBarHeight();
        mLlTop.setLayoutParams(params);

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, mBinding.cvpVideo);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        //title
        mBinding.cvpVideo.getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        mBinding.cvpVideo.getBackButton().setVisibility(View.VISIBLE);

        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption
                .setIsTouchWiget(true)
                .setRotateViewAuto(true)
                .setLockLand(false)
                .setAutoFullWithSize(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(false)
                .setUrl("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4")
                .setReleaseWhenLossAudio(false)
                .setCacheWithPlay(false)
                .setVideoTitle("测试视频")
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        mBinding.cvpVideo.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
                    }
                }).setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        }).build(mBinding.cvpVideo);
        mBinding.cvpVideo.loadCoverImage("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4", R.mipmap.ic_launcher);
    }

    @Override
    public void initEvent() {
        mBinding.cvpVideo.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBinding.cvpVideo.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orientationUtils.getIsLand() != 1) {
                    //直接横屏
                    orientationUtils.resolveByClick();
                }
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                mBinding.cvpVideo.startWindowFullscreen(NewsVideoActivity.this, true, true);
            }
        });
    }

    @Override
    public void initData() {
        mBinding.cvpVideo.startAfterPrepared();
    }

    @Override
    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    /**
     * 解决内存泄露问题
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new ContextWrapper(newBase) {
            @Override
            public Object getSystemService(String name) {
                // 解决 VideoView 中 AudioManager 造成的内存泄漏
                if (Context.AUDIO_SERVICE.equals(name)) {
                    return getApplicationContext().getSystemService(name);
                }
                return super.getSystemService(name);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.cvpVideo.getCurrentPlayer().onVideoPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.cvpVideo.getCurrentPlayer().onVideoResume(false);
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        mBinding.cvpVideo.getCurrentPlayer().release();
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (!isPause) {
            mBinding.cvpVideo.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }

    @Override
    protected boolean isFitWindow() {
        return false;
    }
}
