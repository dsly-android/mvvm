package com.android.dsly.web.base;

import android.app.Application;
import android.util.Log;

import com.android.dsly.common.core.AppLifecycles;
import com.android.dsly.common.core.ConfigModule;
import com.android.dsly.common.utils.ProcessUtils;
import com.blankj.utilcode.util.LogUtils;
import com.tencent.smtt.sdk.QbSdk;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

/**
 * @author 陈志鹏
 * @date 2020/2/27
 */
public class WebConfiguration implements ConfigModule {

    @Override
    public void injectAppLifecycle(Application application, List<AppLifecycles> lifecycles) {
        if (!ProcessUtils.getCurrentProcessName(application).equals(application.getPackageName()+":web")){
            return;
        }
        lifecycles.add(new AppLifecycles() {
            @Override
            public void attachBaseContext(@NonNull Application application) {

            }

            @Override
            public void onCreate(@NonNull Application application) {
                //初始化x5内核
                initX5(application);
            }

            @Override
            public void onTerminate(@NonNull Application application) {

            }
        });
    }

    @Override
    public void injectActivityLifecycle(Application application, List<Application.ActivityLifecycleCallbacks> lifecycles) {

    }

    @Override
    public void injectFragmentLifecycle(Application application, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {

    }

    private void initX5(Application application) {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtils.i(" onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //是否允许非wifi场景下下载内核
        QbSdk.setDownloadWithoutWifi(false);
        //x5内核初始化接口
        QbSdk.initX5Environment(application, cb);
    }
}
