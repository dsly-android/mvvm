package com.android.dsly.common.network;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 陈志鹏
 * @date 2020/10/21
 */
public class BodyInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if ("POST".equals(request.method())||"GET".equals(request.method())) {
            if (request.body() instanceof FormBody) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                FormBody formBody = (FormBody) request.body();
                // 先复制原来的参数
                for (int i = 0; i < formBody.size(); i++) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                }
                formBody = bodyBuilder
                        .addEncoded("公共参数名", "公共参数值")
                        .addEncoded("公共参数名", "公共参数值")
                        .build();
                request = request.newBuilder().post(formBody).build();
            }else {
                HttpUrl httpUrl = request.url()
                        .newBuilder()
                        .addQueryParameter("公共参数名","公共参数值")
                        .build();
                request = request.newBuilder().url(httpUrl).build();
            }
        }
        return chain.proceed(request);
    }
}
