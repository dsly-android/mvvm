package com.htxtdshopping.htxtd.frame.network;

import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;

/**
 * @author 陈志鹏
 * @date 2018/7/30
 */
public class STSProvider extends OSSFederationCredentialProvider {

    @Override
    public OSSFederationToken getFederationToken() {
        //通过接口访问自己的服务器返回数据
        /*Call<BaseResponse<GetAliStsBean>> call = ServerApi.getAliStsResponse();
        try {
            Response<BaseResponse<GetAliStsBean>> response = call.execute();
            BaseResponse<GetAliStsBean> body = response.body();
            GetAliStsBean.CredentialsBean credentials = body.getDomain().getCredentials();
            return new OSSFederationToken(credentials.getAccessKeyId(),credentials.getAccessKeySecret(),
                    credentials.getSecurityToken(),credentials.getExpiration());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return null;
    }
}
