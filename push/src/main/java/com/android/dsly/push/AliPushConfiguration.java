package com.android.dsly.push;

import android.app.Application;
import android.content.Context;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.huawei.HuaWeiRegister;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.alibaba.sdk.android.push.register.OppoRegister;
import com.alibaba.sdk.android.push.register.VivoRegister;
import com.android.dsly.common.core.AppLifecycles;
import com.android.dsly.common.core.ConfigModule;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NotificationUtils;
import com.blankj.utilcode.util.ProcessUtils;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

/**
 * @author 陈志鹏
 * @date 2021/7/8
 */
public class AliPushConfiguration implements ConfigModule {

    @Override
    public void injectAppLifecycle(Application application, List<AppLifecycles> lifecycles) {
        lifecycles.add(new AppLifecycles() {
            @Override
            public void attachBaseContext(@NonNull Application application) {

            }

            @Override
            public void onCreate(@NonNull Application application) {
                PushContext.init(application);
                initCloudChannel(application);
                clearNotification(application);
            }

            @Override
            public void onTerminate(@NonNull Application application) {

            }
        });
    }

    /**
     * 初始化云推送通道
     */
    private void initCloudChannel(Application application) {
        PushServiceFactory.init(application);
        if (ProcessUtils.isMainProcess()) {
            CloudPushService pushService = PushServiceFactory.getCloudPushService();
//          pushService.setLogLevel(CloudPushService.LOG_DEBUG);
            pushService.register(application, new CommonCallback() {
                @Override
                public void onSuccess(String response) {
                    String deviceId = pushService.getDeviceId();
                    //deviceId 是否会发生改变
//                    iOS：不会改变 。
//                    Android：
//                    卸载重装可能会变 。
//                    极个别机型，授予文件读取和手机识别码权限后 ，改变一次 。
                }

                @Override
                public void onFailed(String errorCode, String errorMessage) {
                    LogUtils.e("init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
                }
            });

            MiPushRegister.register(application, "2882303761519981571", "5411998122571");
            OppoRegister.register(application, "416af62071184ddf88003b3c78ea5f8b", "4a86f191ba614930b29f62a95fc18c6b");
            HuaWeiRegister.register(application);
            VivoRegister.register(application);
        }
    }

    /**
     * 打开应用的时候清除通知
     */
    private static void clearNotification(Context context) {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            public void onForeground() {
                //阿里推送清除通知，不能清除系统推送的通知
                PushServiceFactory.getCloudPushService().clearNotifications();
                NotificationUtils.cancelAll();
                //小米推送清除系统通知
                MiPushClient.clearNotification(context);
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            public void onBackground() {
            }
        });
    }

    @Override
    public void injectActivityLifecycle(Application application, List<Application.ActivityLifecycleCallbacks> lifecycles) {

    }

    @Override
    public void injectFragmentLifecycle(Application application, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {

    }
}