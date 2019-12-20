package com.htxtdshopping.htxtd.frame.di.component;

import com.android.dsly.common.di.module.BaseModule;
import com.htxtdshopping.htxtd.frame.base.App;
import com.htxtdshopping.htxtd.frame.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, BaseModule.class, AppModule.class})
public interface AppComponent extends AndroidInjector<App> {

}