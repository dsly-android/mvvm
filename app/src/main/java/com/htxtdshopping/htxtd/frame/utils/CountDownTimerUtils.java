package com.htxtdshopping.htxtd.frame.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 倒计时工具类
 *
 * @author 陈志鹏
 * @date 2021/4/27
 */
public class CountDownTimerUtils extends CountDownTimer {
    private TextView mTextView;

    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval) {
        //millisInFuture：倒计时的总时长
        //countDownInterval：每次的间隔时间  单位都是毫秒
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (mTextView != null) {
            mTextView.setClickable(false); //设置不可点击
            mTextView.setText(millisUntilFinished / 1000 + "秒");  //设置倒计时时间
        }
    }

    @Override
    public void onFinish() {
        if (mTextView != null) {
            mTextView.setClickable(true);//重新获得点击
            mTextView.setText("获取验证码");
        }
    }
}