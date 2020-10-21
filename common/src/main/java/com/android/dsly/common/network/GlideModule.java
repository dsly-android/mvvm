package com.android.dsly.common.network;

import android.content.Context;

import com.android.dsly.rxhttp.utils.HttpsUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;

/**
 * glide加载https图片问题
 * @author 陈志鹏
 * @date 2020/10/21
 */
@com.bumptech.glide.annotation.GlideModule
public class GlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //1、信任所有证书,不安全有风险（默认信任所有证书）
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(builder.build()));
    }
}