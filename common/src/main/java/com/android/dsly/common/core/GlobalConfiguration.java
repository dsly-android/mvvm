package com.android.dsly.common.core;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.dsly.common.BuildConfig;
import com.android.dsly.common.notification.NotificationChannels;
import com.android.dsly.rxhttp.RxHttp;
import com.android.dsly.rxhttp.cache.CacheEntity;
import com.android.dsly.rxhttp.cache.CacheMode;
import com.android.dsly.rxhttp.cookie.store.SPCookieStore;
import com.android.dsly.rxhttp.interceptor.HttpLoggingInterceptor;
import com.android.dsly.rxhttp.utils.HttpsUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.tencent.smtt.sdk.QbSdk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.multidex.MultiDex;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;

/**
 * 全局配置
 *
 * @author 陈志鹏
 * @date 2019-12-11
 */
public class GlobalConfiguration implements ConfigModule {

    @Override
    public void injectAppLifecycle(Application application, List<AppLifecycles> lifecycles) {
        lifecycles.add(new AppLifecycles() {
            @Override
            public void attachBaseContext(@NonNull Application app) {
                //      如果需要使用MultiDex，需要在此处调用。
                MultiDex.install(app);
            }

            @Override
            public void onCreate(@NonNull Application app) {
                //初始化RxHttp
                initRxHttp(app);
                //初始化AndroidAutoSize
                initAutoSize(app);
                //初始化AndroidUtilCode
                Utils.init(app);
                //初始化log
                initLog();
                //初始化奔溃重启和奔溃日志
                if (!AppUtils.isAppDebug()) {
                    initCrash();
                }
                //初始化x5内核
                initX5(app);
                //初始化通知渠道
                initNotificationChannels(app);
                //ARouter
                initARouter(app);
            }

            @Override
            public void onTerminate(@NonNull Application app) {

            }
        });
    }

    @Override
    public void injectActivityLifecycle(Application application, List<Application.ActivityLifecycleCallbacks> lifecycles) {

    }

    @Override
    public void injectFragmentLifecycle(Application application, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {

    }

    private void initARouter(Application app){
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(app); // 尽可能早，推荐在Application中初始化
    }

    private void initRxHttp(Application application) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //全局的读取超时时间
        builder.readTimeout(RxHttp.DEFAULT_MILLISECONDS / 2, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(RxHttp.DEFAULT_MILLISECONDS / 2, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(RxHttp.DEFAULT_MILLISECONDS / 2, TimeUnit.MILLISECONDS);

        //1、信任所有证书,不安全有风险（默认信任所有证书）
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        //2、使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //3、使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(bksInputStream,"123456",cerInputStream);
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);

        //设置Hostname校验规则，默认实现返回true，需要时候传入相应校验规则即可
        //builder.hostnameVerifier(null);

        if (BuildConfig.DEBUG) {
            //log相关
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("RxHttp");
            //log打印级别，决定了log显示的详细程度
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
            //log颜色级别，决定了log在控制台显示的颜色
            loggingInterceptor.setColorLevel(Level.INFO);
            //添加RxHttp默认debug日志
            builder.addInterceptor(loggingInterceptor);
        }

        RxHttp.getInstance()
                .setBaseUrl("https://www.apiopen.top/")
                .setOkHttpClientBuild(RetrofitUrlManager.getInstance().with(builder))
//                .addCommonHeader("aaa","aaa")  //全局公共头
//                .addCommonHeaders(new LinkedHashMap<String, String>())   //全局公共头
                .setCookieType(new SPCookieStore(application)) //使用sp保持cookie，如果cookie不过期，则一直有效
//                .setCookieType(new MemoryCookieStore()) //使用内存保持cookie，app退出后，cookie消失
//                .setCookieType(new DBCookieStore(this)) ////使用数据库保持cookie，如果cookie不过期，则一直有效
                .setCacheMode(CacheMode.NO_CACHE) //缓存模式，默认不缓存
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE) //缓存过期时间，默认远不过期
                .init(application);
    }

    private void initAutoSize(Application application) {
        //当 App 中出现多进程, 并且您需要适配所有的进程, 就需要在 App 初始化时调用 initCompatMultiProcess()
        AutoSize.initCompatMultiProcess(application);
        AutoSizeConfig.getInstance().setLog(BuildConfig.DEBUG);
        AutoSizeConfig.getInstance().setExcludeFontScale(true);
        AutoSizeConfig.getInstance().getUnitsManager()
                .setSupportSubunits(Subunits.PT)
                .setSupportDP(false)
                .setSupportSP(true);
    }

    private void initLog() {
        LogUtils.getConfig()
                // 设置 log 总开关，包括输出到控制台和文件，默认开
                .setLogSwitch(BuildConfig.DEBUG)
                // 设置是否输出到控制台开关，默认开
                .setConsoleSwitch(BuildConfig.DEBUG)
                // 设置 log 全局标签，默认为空
                .setGlobalTag(null)
                // 当全局标签不为空时，我们输出的 log 全部为该 tag，
                // 为空时，如果传入的 tag 为空那就显示类名，否则显示 tag
                // 设置 log 头信息开关，默认为开
                .setLogHeadSwitch(true)
                // 打印 log 时是否存到文件的开关，默认关
                .setLog2FileSwitch(false)
                // 当自定义路径为空时，写入应用的/cache/log/目录中
                .setDir("")
                // 当文件前缀为空时，默认为"util"，即写入文件为"util-yyyy-MM-dd.txt"
                .setFilePrefix("")
                // 输出日志是否带边框开关，默认开
                .setBorderSwitch(true)
                // 一条日志仅输出一条，默认开，为美化 AS 3.1 的 Logcat
                .setSingleTagSwitch(true)
                // log 的控制台过滤器，和 logcat 过滤器同理，默认 Verbose
                .setConsoleFilter(LogUtils.V)
                // log 文件过滤器，和 logcat 过滤器同理，默认 Verbose
                .setFileFilter(LogUtils.V)
                // log 栈深度，默认为 1
                .setStackDeep(1)
                // 设置栈偏移，比如二次封装的话就需要设置，默认为 0
                .setStackOffset(0)
                // 设置日志可保留天数，默认为 -1 表示无限时长
                .setSaveDays(3)
                // 新增 ArrayList 格式化器，默认已支持 Array, Throwable, Bundle, Intent 的格式化输出
                .addFormatter(new LogUtils.IFormatter<ArrayList>() {
                    @Override
                    public String format(ArrayList list) {
                        return "LogUtils Formatter ArrayList { " + list.toString() + " }";
                    }
                });
    }

    @SuppressLint("MissingPermission")
    private void initCrash() {
        CrashUtils.init(new CrashUtils.OnCrashListener() {
            @Override
            public void onCrash(String crashInfo, Throwable e) {
                LogUtils.e(Log.getStackTraceString(e));
                AppUtils.relaunchApp();
            }
        });
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

    private void initNotificationChannels(Application application) {
        NotificationChannels.createAllNotificationChannels(application);
    }
}