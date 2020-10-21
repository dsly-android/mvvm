/*
 * Copyright 2017 JessYan
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

import com.android.dsly.common.network.BaseResponse;
import com.htxtdshopping.htxtd.frame.bean.CallBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface CommonApi {

    @GET
    Observable<ResponseBody> getBitmap(@Url String url);

    /**
     * 语音通话--呼叫
     *
     * @param receiverId
     * @param roomId
     * @return
     */
    @POST("/v1/voice/call.do")
    @FormUrlEncoded
    Observable<BaseResponse<CallBean>> call(@Field("receiverId") String receiverId, @Field("roomId") String roomId);

    /**
     * 语音通话--挂断 -- udp端也要挂断，以udp为主，如果udp挂断消息未推送成功，由webSocket推送挂断消息，客户端收到做同样的处理
     */
    @POST("/v1/voice/hangUp.do")
    @FormUrlEncoded
    Observable<BaseResponse> hangUp(@Field("receiverId") String receiverId, @Field("roomId") String roomId);
}