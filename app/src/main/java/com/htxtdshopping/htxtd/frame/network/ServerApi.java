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

import com.android.dsly.common.network.BaseResponse;
import com.android.dsly.common.network.DataObserver;
import com.android.dsly.rxhttp.RxHttp;
import com.android.dsly.rxhttp.utils.TransformerUtils;
import com.htxtdshopping.htxtd.frame.bean.CallBean;
import com.htxtdshopping.htxtd.frame.event.VersionUpdateEvent;
import com.htxtdshopping.htxtd.frame.utils.AppSelfSPUtils;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.RequestBody;
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
     * 版本更新接口GSYVideoOptionBuilder
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

    /**
     * test
     */
    public static void test(){
        RxHttp.createApi(CommonApi.class)
                .call("1","1")
                .map(new Function<BaseResponse<CallBean>, BaseResponse<CallBean>>() {
                    @Override
                    public BaseResponse<CallBean> apply(@NonNull BaseResponse<CallBean> response) throws Exception {
                        //如果服务器报错或者没有网络不会走这个方法，只有正常返回才会走这个方法
                        if (response.getCode() == BaseResponse.SUCCESS){
                            //只要有返回结果就会执行这个方法，需要判断返回的结果是否是成功的
                        }
                        return response;
                    }
                }).compose(TransformerUtils.pack())
                .subscribe(new DataObserver<>());
    }

    /**
     * POST提交application/json数据
     */
    public static void publish(String json){
        RxHttp.createApi(CommonApi.class)
                .publish(generateRequestBody(json))
                .compose(TransformerUtils.pack())
                .subscribe(new DataObserver<>());
    }

    private static RequestBody generateRequestBody(String json){
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), json);
    }

    /**
     * 分页数据只缓存第一页
     */
    /*public static void circle(Long userId, int page, LifecycleProvider provider,
                              MutableLiveData<RxHttpResponse<BaseResponse<List<DynamicCircleBean>>>> liveData) {
        if (page == 1) {
            RxHttp.createApi(ChatApi.class)
                    .circle(userId, page)
                    .compose(TransformerUtils.cachePackResp(provider, generateCacheKey("ChatServerApi-circle-" + page)))
                    .subscribe(new DataObserver(liveData));
        } else {
            RxHttp.createApi(ChatApi.class)
                    .circle(userId, page)
                    .compose(TransformerUtils.noCachePackResp(provider))
                    .subscribe(new DataObserver(liveData));
        }
    }*/

    /**
     * userId用来标示哪个用户的缓存
     * key：类名-方法名-参数
     */
    public static String generateCacheKey(String key) {
        long userId = AppSelfSPUtils.getUserId();
        key = userId + "-" + key;
        return key;
    }
}