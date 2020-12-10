package com.android.dsly.common.event;

import com.blankj.utilcode.util.NetworkUtils;

/**
 * @author 陈志鹏
 * @date 2020/12/10
 */
public class NetworkStatusChangedEvent {

    //是否有网络连接
    private boolean mIsConnected = false;

    //网络类型
    private NetworkUtils.NetworkType mNetworkType;

    public boolean isConnected() {
        return mIsConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.mIsConnected = isConnected;
    }

    public NetworkUtils.NetworkType getNetworkType() {
        return mNetworkType;
    }

    public void setNetworkType(NetworkUtils.NetworkType networkType) {
        this.mNetworkType = networkType;
    }
}
