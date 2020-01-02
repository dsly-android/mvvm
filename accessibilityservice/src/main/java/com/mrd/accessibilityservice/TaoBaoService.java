package com.mrd.accessibilityservice;

import android.view.accessibility.AccessibilityEvent;

import com.blankj.utilcode.util.LogUtils;

/**
 * @author 陈志鹏
 * @date 2020-01-01
 */
public class TaoBaoService extends BaseAccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        LogUtils.e(Thread.currentThread().getName());//main线程中运行
        /*Observable.timer(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        clickTextViewByText("取消");
                        SystemClock.sleep(1000);
                        //返回桌面
                        ActivityUtils.startHomeActivity();
                        SystemClock.sleep(1000);
                        //启动淘宝
                        AppUtils.launchApp("com.taobao.taobao");
                    }
                });*/
    }

    @Override
    public void onInterrupt() {

    }
}
