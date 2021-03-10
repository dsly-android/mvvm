package com.htxtdshopping.htxtd.frame.base;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.dsly.common.base.BaseApp;
import com.android.dsly.common.constant.Constants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ProcessUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.htxtdshopping.htxtd.frame.BuildConfig;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.network.OssService;
import com.htxtdshopping.htxtd.frame.ui.other.activity.SplashActivity;
import com.htxtdshopping.htxtd.frame.widget.refresh.NewsRefreshHeader;
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher;
import com.lzf.easyfloat.EasyFloat;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.ui.UILifecycleListener;
import com.tencent.bugly.beta.upgrade.UpgradeStateListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * @author 陈志鹏
 * @date 2018/7/27
 */
public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        if (!ProcessUtils.getCurrentProcessName().equals(getPackageName())){
            return;
        }

        AppContext.init(this);
        //bugly
        initBugly();
        //oss
        initOss();
        //初始化UShare
        initUShare();
        //okdownload
        initOkDownload();
        //EasyFloat
        initEasyFloat();
    }

    static {//使用static代码段可以防止内存泄漏
        //启用矢量图兼容
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                //开始设置全局的基本参数
                layout.setEnableLoadMore(false);
                layout.setEnableAutoLoadMore(true);
                layout.setEnableOverScrollDrag(false);
                layout.setEnableOverScrollBounce(true);
                layout.setEnableLoadMoreWhenContentNotFull(false);
                layout.setEnableScrollContentWhenRefreshed(true);
                layout.setEnableFooterFollowWhenLoadFinished(true);
            }
        });

        //全局设置默认的 Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //开始设置全局的基本参数（这里设置的属性只跟下面的MaterialHeader绑定，其他Header不会生效，能覆盖DefaultRefreshInitializer的属性和Xml设置的属性）
                //全局设置主题颜色（优先级第二低，可以覆盖 DefaultRefreshInitializer 的配置，与下面的ClassicsHeader绑定）
                layout.setPrimaryColorsId(android.R.color.white, R.color._81D8CF);
                return new NewsRefreshHeader(context);
            }
        });
    }

    private void initBugly() {
        String currentProcessName = ProcessUtils.getCurrentProcessName();
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setUploadProcess(currentProcessName == null || currentProcessName.equals(getPackageName()));

        Beta.storageDir = new File(getFilesDir().getAbsolutePath() + "/bugly");
        Beta.enableHotfix = false;
        //添加不显示更新弹窗的activity
        Beta.canNotShowUpgradeActs.add(SplashActivity.class);
        //Wifi下自动下载,默认值为false。
//        Beta.autoDownloadOnWifi = false;
        Beta.upgradeDialogLayoutId = R.layout.dialog_upgrade;
        Beta.upgradeStateListener = new UpgradeStateListener() {
            @Override
            public void onUpgradeFailed(boolean b) {
                if (b) {
                    ToastUtils.showLong("检查更新失败");
                }
            }

            @Override
            public void onUpgradeSuccess(boolean b) {

            }

            @Override
            public void onUpgradeNoVersion(boolean b) {
                if (b) {
                    ToastUtils.showLong("你已经是最新版了");
                }
            }

            @Override
            public void onUpgrading(boolean b) {

            }

            @Override
            public void onDownloadCompleted(boolean b) {

            }
        };
        //升级对话框生命周期回调接口
        Beta.upgradeDialogLifecycleListener = new UILifecycleListener<UpgradeInfo>() {
            @Override
            public void onCreate(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onStart(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onResume(Context context, View view, UpgradeInfo upgradeInfo) {
                TextView tvTitle = view.findViewWithTag("beta_title");
                tvTitle.setText(AppUtils.getAppName());
            }

            @Override
            public void onPause(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onStop(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onDestroy(Context context, View view, UpgradeInfo upgradeInfo) {

            }
        };

        // 初始化Bugly
        Bugly.init(getApplicationContext(), "38552d428b", BuildConfig.DEBUG, strategy);
    }

    private void initOss() {
        OssService.init(Constants.OSS_BUCKET);
    }

    private void initUShare() {
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
    }

    {
        //微信
        PlatformConfig.setWeixin(Constants.WX_APP_ID, Constants.WX_APP_SECRET);
    }

    private void initOkDownload() {
        DownloadDispatcher.setMaxParallelRunningCount(3);
    }

    private void initEasyFloat() {
        EasyFloat.init(this, BuildConfig.DEBUG);
    }
}