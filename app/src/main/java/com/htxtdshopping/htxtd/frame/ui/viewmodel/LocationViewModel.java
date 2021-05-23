package com.htxtdshopping.htxtd.frame.ui.viewmodel;

import android.app.Application;

import com.android.dsly.common.base.BaseViewModel;

import androidx.annotation.NonNull;

/**
 * @author 陈志鹏
 * @date 2021/3/15
 */
public class LocationViewModel extends BaseViewModel {

    public LocationViewModel(@NonNull Application application) {
        super(application);
//        init();
    }

    /*private MutableLiveData<AMapLocation> mLocationLiveData = new MutableLiveData<>();

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    int locationType = aMapLocation.getLocationType();
                    //获取经度
                    double longitude = aMapLocation.getLongitude();
                    //获取纬度
                    double latitude = aMapLocation.getLatitude();
                    LogUtils.i(Thread.currentThread().getName() + "  " + locationType + "    " + longitude + "    " + latitude);

                    mLocationLiveData.setValue(aMapLocation);
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    LogUtils.e("location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    public void init() {
        //初始化定位
        mLocationClient = new AMapLocationClient(Utils.getApp());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        AMapLocationClientOption option = new AMapLocationClientOption();
        *//**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         *//*
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        option.setOnceLocationLatest(true);
        mLocationClient.setLocationOption(option);
    }

    public void start() {
        if (null != mLocationClient) {
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    public void stop() {
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
        mLocationListener = null;
    }

    public MutableLiveData<AMapLocation> getLocationLiveData() {
        return mLocationLiveData;
    }*/
}