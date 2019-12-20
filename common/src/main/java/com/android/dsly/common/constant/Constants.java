package com.android.dsly.common.constant;

import com.blankj.utilcode.util.Utils;

/**
 * @author 陈志鹏
 * @date 2018/11/21
 */
public interface Constants {
    /**
     * 临时图片缓存路径
     */
    String PATH_EXTERNAL_CACHE_IMAGE = Utils.getApp().getExternalCacheDir() + "/image/";
    String PATH_CACHE_IMAGE = Utils.getApp().getCacheDir() + "/image/";
    //文件下载路径
    String PATH_EXTERNAL_CACHE_DOWNLOAD = Utils.getApp().getExternalCacheDir() + "/download/";

    String OSS_ENDPOINT = "http://oss-cn-shenzhen.aliyuncs.com";
    String OSS_BUCKET = "youya-bucket-1";

    //微信appId
    String WX_APP_ID = "wx88888888";
    //微信appSecret
    String WX_APP_SECRET = "afewfaflakfd";

    /**
     * 通知id
     */
    //版本更新
    int NOTIFICATION_UPGRADE = 1;
}