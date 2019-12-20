/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.htxtdshopping.htxtd.frame.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.dsly.rxhttp.RxHttp;
import com.htxtdshopping.htxtd.frame.event.VersionUpdateEvent;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * @author chenzhipeng
 */
public class ServerApi {

    public static Observable<Bitmap> getBitmap(String imgUrl) {
        return RxHttp.createApi(CommonApi.class)
                .getBitmap(imgUrl)
                .map(new Function<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap apply(ResponseBody responseBody) throws Exception {
                        byte[] bytes = responseBody.bytes();
                        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    }
                });
    }

    /**
     * 版本更新接口
     */
    public static VersionUpdateEvent versionUpdate() {
        VersionUpdateEvent event = new VersionUpdateEvent();
        event.setForce(false);
        event.setVersionCode(2);
        event.setVersionName("1.1.0");
        event.setApkUrl("https://6be0527b4ce07d30a8260fe78599460c.dd.cdntips.com/imtt.dd.qq.com/16891/apk/4930D062BB950D0CB156EDA29DF813C0.apk?mkey=5d302acf7ae0dcd2&f=1026&fsname=com.ss.android.article.video_3.7.4_374.apk&csr=1bbd&cip=122.224.250.39&proto=https");
        event.setDescription("1.支持断点下载\n2.支持Android N\n3.支持Android O\n4.支持Android P\n5.支持自定义下载过程\n6.支持 设备>=Android M 动态权限的申请\n7.支持通知栏进度条展示");
        return event;
    }
}