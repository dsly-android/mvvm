package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.LogUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityDistanceSensorBinding;

public class DistanceSensorActivity extends BaseActivity<ActivityDistanceSensorBinding, BaseViewModel> implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;

    @Override
    public int getLayoutId() {
        return R.layout.activity_distance_sensor;
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void initView(Bundle savedInstanceState) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //息屏设置
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "TAG");
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册传感器,先判断有没有传感器
        if (mSensor != null)
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * 发生新的传感器事件时调用
     *
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        LogUtils.i("onSensorChanged:" + event.values[0] + "");
        if (event.values[0] == 0.0) {
            //关闭屏幕
            if (!mWakeLock.isHeld())
                mWakeLock.acquire();
        } else {
            //唤醒设备
            if (mWakeLock.isHeld())
                mWakeLock.release();
        }
    }

    /**
     * 当注册的传感器的精度改变时调用。 与onSensorChanged（）不同，仅当此精度值更改时才调用此方法。
     *
     * @param sensor
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        LogUtils.i("onAccuracyChanged:" + accuracy + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //传感器取消监听
        mSensorManager.unregisterListener(this);
        //释放息屏
        if (mWakeLock.isHeld())
            mWakeLock.release();
        mWakeLock = null;
        mPowerManager = null;
    }
}
