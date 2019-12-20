package com.htxtdshopping.htxtd.frame.ui.second.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.dsly.common.base.BaseActivity;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 陈志鹏
 * @date 2018/11/23
 */
public class UpgradeActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.tv_size)
    TextView mTvSize;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.btn_start)
    Button mBtnStart;

    @Override
    public int getLayoutId() {
        return R.layout.activity_upgrade;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawableResource(R.drawable.bg_white_corner20);

        BarUtils.setStatusBarVisibility(this, false);

        /*获取策略信息，初始化界面信息*/
        mTvTitle.setText(mTvTitle.getText().toString() + Beta.getUpgradeInfo().title);
        mTvVersion.setText(mTvVersion.getText().toString() + Beta.getUpgradeInfo().versionName);
        mTvSize.setText(mTvSize.getText().toString() + ConvertUtils.byte2FitMemorySize(Beta.getUpgradeInfo().fileSize));
        mTvTime.setText(mTvTime.getText().toString() + TimeUtils.millis2String(Beta.getUpgradeInfo().publishTime));
        mTvContent.setText(Beta.getUpgradeInfo().newFeature);

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
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_cancel, R.id.btn_start})
    public void onViewClicked(View view) {
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
                mBtnStart.setText("开始下载");
                break;
            case DownloadTask.COMPLETE:
                mBtnStart.setText("安装");
                break;
            case DownloadTask.DOWNLOADING:
                mBtnStart.setText("暂停");
                break;
            case DownloadTask.PAUSED:
                mBtnStart.setText("继续下载");
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