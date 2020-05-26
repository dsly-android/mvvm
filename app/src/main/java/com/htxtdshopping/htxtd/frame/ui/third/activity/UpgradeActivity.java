package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.constant.Constants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.base.AppContext;
import com.htxtdshopping.htxtd.frame.databinding.ActivityUpgradeBinding;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.DownloadListener1;
import com.liulishuo.okdownload.core.listener.assist.Listener1Assist;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author 陈志鹏
 * @date 2019-08-06
 */
public class UpgradeActivity extends BaseActivity<ActivityUpgradeBinding, BaseViewModel> implements View.OnClickListener {

    public static final String VERSIONCODE = "versionCode";
    public static final String VERSIONNAME = "versionName";
    public static final String APKURL = "apkUrl";
    public static final String ISFORCE = "isForce";
    public static final String DESCRIPTION = "description";

    private int mVersionCode;
    private String mVersionName;
    private String mApkUrl;
    private boolean mIsForce;
    private String mDescription;

    private DownloadTask mTask;
    private DownloadListener1 mListener;

    @Override
    public int getLayoutId() {
        return R.layout.activity_upgrade;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        BarUtils.setStatusBarVisibility(this, false);

        mVersionCode = getIntent().getIntExtra(VERSIONCODE, AppUtils.getAppVersionCode());
        mVersionName = getIntent().getStringExtra(VERSIONNAME);
        mApkUrl = getIntent().getStringExtra(APKURL);
        mIsForce = getIntent().getBooleanExtra(ISFORCE, false);
        mDescription = getIntent().getStringExtra(DESCRIPTION);

        /*获取策略信息，初始化界面信息*/
        mBinding.tvTitle.setText(mBinding.tvTitle.getText().toString() + mVersionCode);
        mBinding.tvVersion.setText(mBinding.tvVersion.getText().toString() + mVersionName);
        mBinding.tvContent.setText(mDescription);
        if (mIsForce) {
            mBinding.btnCancel.setVisibility(View.GONE);
            setFinishOnTouchOutside(false);
        } else {
            mBinding.btnCancel.setVisibility(View.VISIBLE);
            setFinishOnTouchOutside(true);
        }

        String apkUrl = "https://c9dec4e5c25870b8d86ff783ba4e7d2a.dd.cdntips.com/imtt.dd.qq.com/16891/apk/9F7457E13763356C44B14D7DF226044B.apk?mkey=5d4b8b8b751e129b&f=0c58&fsname=com.ss.android.article.news_7.3.6_736.apk&csr=1bbd&cip=117.30.52.110&proto=https";
        mTask = new DownloadTask.Builder(apkUrl, new File(Constants.PATH_EXTERNAL_CACHE_DOWNLOAD))
                .setFilename("今日头条.apk")
                // the minimal interval millisecond for callback progress
                .setMinIntervalMillisCallbackProcess(100)
                .build();

        /*获取下载任务，初始化界面信息*/
        initStatus();
    }

    @Override
    public void initEvent() {
        /*注册下载监听，监听下载事件*/
        mListener = new DownloadListener1() {

            @Override
            public void taskStart(@NonNull DownloadTask task, @NonNull Listener1Assist.Listener1Model model) {
                mBinding.btnCancel.setVisibility(View.GONE);
                mBinding.btnStart.setVisibility(View.GONE);
                mBinding.btnInstall.setVisibility(View.GONE);
                mBinding.pbProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void retry(@NonNull DownloadTask task, @NonNull ResumeFailedCause cause) {

            }

            @Override
            public void connected(@NonNull DownloadTask task, int blockCount, long currentOffset, long totalLength) {

            }

            @Override
            public void progress(@NonNull DownloadTask task, long currentOffset, long totalLength) {
                calcProgressToView(currentOffset, totalLength);
            }

            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull Listener1Assist.Listener1Model model) {
                initStatus();
            }
        };

        mBinding.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    private void initStatus() {
        StatusUtil.Status status = StatusUtil.getStatus(mTask);
        if (status == StatusUtil.Status.COMPLETED) {
            mBinding.btnStart.setVisibility(View.GONE);
            mBinding.btnInstall.setVisibility(View.VISIBLE);
            mBinding.pbProgress.setVisibility(View.GONE);
            mBinding.pbProgress.setProgress(mBinding.pbProgress.getMax());
            if (mIsForce) {
                mBinding.btnCancel.setVisibility(View.GONE);
            } else {
                mBinding.btnCancel.setVisibility(View.VISIBLE);
            }
        }
        BreakpointInfo info = StatusUtil.getCurrentInfo(mTask);
        if (info != null) {
            calcProgressToView(info.getTotalOffset(), info.getTotalLength());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                mTask.cancel();
                finish();
                break;
            case R.id.btn_start:
                if (mIsForce) {
                    mBinding.btnStart.setVisibility(View.GONE);
                    mBinding.pbProgress.setVisibility(View.VISIBLE);
                    mTask.enqueue(mListener);
                } else {
                    mTask.enqueue(AppContext.getInstance().getUpgradeListener());
                    finish();
                }
                break;
            case R.id.btn_install:
                AppUtils.installApp(Constants.PATH_EXTERNAL_CACHE_DOWNLOAD + "今日头条.apk");
                break;
            default:
                break;
        }
    }

    private void calcProgressToView(long offset, long total) {
        float percent = (float) offset / total;
        mBinding.pbProgress.setProgress((int) (percent * mBinding.pbProgress.getMax()));
    }

    @Override
    public void onBackPressed() {
        if (!mIsForce) {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsForce) {
            mTask.cancel();
        }
    }
}
