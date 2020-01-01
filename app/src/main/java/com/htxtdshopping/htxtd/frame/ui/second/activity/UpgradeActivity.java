package com.htxtdshopping.htxtd.frame.ui.second.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityUpgradeBinding;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;

/**
 * @author 陈志鹏
 * @date 2018/11/23
 */
public class UpgradeActivity extends BaseActivity<ActivityUpgradeBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_upgrade;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawableResource(R.drawable.bg_white_corner20);

        BarUtils.setStatusBarVisibility(this, false);

        /*获取策略信息，初始化界面信息*/
        mBinding.tvTitle.setText(mBinding.tvTitle.getText().toString() + Beta.getUpgradeInfo().title);
        mBinding.tvVersion.setText(mBinding.tvVersion.getText().toString() + Beta.getUpgradeInfo().versionName);
        mBinding.tvSize.setText( mBinding.tvSize.getText().toString() + ConvertUtils.byte2FitMemorySize(Beta.getUpgradeInfo().fileSize));
        mBinding.tvTime.setText(mBinding.tvTime.getText().toString() + TimeUtils.millis2String(Beta.getUpgradeInfo().publishTime));
        mBinding.tvContent.setText(Beta.getUpgradeInfo().newFeature);

        /*获取下载任务，初始化界面信息*/
        updateBtn(Beta.getStrategyTask());
    }

    @Override
    public void initEvent() {
        /*注册下载监听，监听下载事件*/
        Beta.registerDownloadListener(new DownloadListener() {
            @Override
            public void onReceive(DownloadTask task) {
                updateBtn(task);
            }

            @Override
            public void onCompleted(DownloadTask task) {
                updateBtn(task);
            }

            @Override
            public void onFailed(DownloadTask task, int code, String extMsg) {
                updateBtn(task);
            }
        });
        mBinding.btnCancel.setOnClickListener(this);
        mBinding.btnStart.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                Beta.cancelDownload();
                finish();
                break;
            case R.id.btn_start:
                DownloadTask task = Beta.startDownload();
                updateBtn(task);
                finish();
                break;
            default:
                break;
        }
    }

    public void updateBtn(DownloadTask task) {
        /*根据下载任务状态设置按钮*/
        switch (task.getStatus()) {
            case DownloadTask.INIT:
            case DownloadTask.DELETED:
            case DownloadTask.FAILED:
                mBinding.btnStart.setText("开始下载");
                break;
            case DownloadTask.COMPLETE:
                mBinding.btnStart.setText("安装");
                break;
            case DownloadTask.DOWNLOADING:
                mBinding.btnStart.setText("暂停");
                break;
            case DownloadTask.PAUSED:
                mBinding.btnStart.setText("继续下载");
                break;
            default:
                break;
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
        /*注销下载监听*/
        Beta.unregisterDownloadListener();
    }
}