package com.android.dsly.common.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author 陈志鹏
 * @date 2019-08-04
 */
@Module
public class BaseModule {

    @Singleton
    @Provides
    public String aaa(){
        return "";
    }
}