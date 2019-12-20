package com.android.dsly.common.core;

import android.app.Application;

import java.util.List;

import androidx.fragment.app.FragmentManager;

/**
 * ================================================
 * {@link ConfigModule} 可以给框架配置一些参数,需要实现 {@link ConfigModule} 后,在 AndroidManifest 中声明该实现类
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.1">ConfigModule wiki 官方文档</a>
 * Created by JessYan on 12/04/2017 11:37
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface ConfigModule {

    /**
     * 使用{@link AppLifecycles}在Application的生命周期中注入一些操作
     *
     * @param application
     * @param lifecycles
     */
    void injectAppLifecycle(Application application, List<AppLifecycles> lifecycles);

    /**
     * 使用{@link Application.ActivityLifecycleCallbacks}在Activity的生命周期中注入一些操作
     *
     * @param application
     * @param lifecycles
     */
    void injectActivityLifecycle(Application application, List<Application.ActivityLifecycleCallbacks> lifecycles);


    /**
     * 使用{@link FragmentManager.FragmentLifecycleCallbacks}在Fragment的生命周期中注入一些操作
     *
     * @param application
     * @param lifecycles
     */
    void injectFragmentLifecycle(Application application, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles);
}