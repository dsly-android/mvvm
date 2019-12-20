package com.htxtdshopping.htxtd.frame.event;

/**
 * @author 陈志鹏
 * @date 2019-08-06
 */
public class VersionUpdateEvent {
    //版本号
    private int versionCode;
    //版本名
    private String versionName;
    //apk下载路径
    private String apkUrl;
    //是否强制更新
    private boolean isForce;
    //版本描述
    private String description;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public boolean isForce() {
        return isForce;
    }

    public void setForce(boolean force) {
        isForce = force;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}