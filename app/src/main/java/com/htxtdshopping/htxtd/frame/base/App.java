package com.htxtdshopping.htxtd.frame.base;

import android.content.Context;
import android.content.Intent;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.android.dsly.common.base.BaseApp;
import com.android.dsly.common.constant.Constants;
import com.android.dsly.common.utils.ToastUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ProcessUtils;
import com.htxtdshopping.htxtd.frame.BuildConfig;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.network.OssService;
import com.htxtdshopping.htxtd.frame.network.STSProvider;
import com.htxtdshopping.htxtd.frame.ui.second.activity.UpgradeActivity;
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
import com.tencent.bugly.beta.upgrade.UpgradeListener;
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
        Beta.upgradeListener = new UpgradeListener() {
            @Override
            public void onUpgrade(int i, UpgradeInfo upgradeInfo, boolean b, boolean b1) {
                if (upgradeInfo != null) {
                    Intent intent = new Intent(getApplicationContext(), UpgradeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }/* else {
                    ToastUtils.showLong("你已经是最新版了");
                }*/
            }
        };
        Beta.upgradeStateListener = new UpgradeStateListener() {
            @Override
            public void onUpgradeFailed(boolean b) {
                ToastUtils.showLong("检查更新失败");
            }

            @Override
            public void onUpgradeSuccess(boolean b) {

            }

            @Override
            public void onUpgradeNoVersion(boolean b) {
//                ToastUtils.showLong("你已经是最新版了");
            }

            @Override
            public void onUpgrading(boolean b) {
                if (b) {
                    ToastUtils.showLong("正在检查更新，请稍后...");
                }
            }

            @Override
            public void onDownloadCompleted(boolean b) {
//                ToastUtils.showLong("下载完成");
                //安装apk
                AppUtils.installApp(Beta.getStrategyTask().getSaveFile());
            }
        };

        // 初始化Bugly
        Bugly.init(getApplicationContext(), "38552d428b", false, strategy);
    }

    private void initOss() {
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

        OSS oss = new OSSClient(this, Constants.OSS_ENDPOINT, credentialProvider, conf);
        OssService.init(oss, Constants.OSS_BUCKET);
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