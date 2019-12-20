package com.android.dsly.common.di.component;

import com.android.dsly.common.base.BaseApp;
import com.android.dsly.common.di.module.BaseModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, BaseModule.class})
public interface BaseComponent extends AndroidInjector<BaseApp> {

}