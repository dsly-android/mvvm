package com.android.dsly.common.viewadapter.view;

import android.view.View;

import com.jakewharton.rxbinding3.view.RxView;

import java.util.concurrent.TimeUnit;

import androidx.databinding.BindingAdapter;
import io.reactivex.functions.Consumer;
import kotlin.Unit;

/**
 * Created by goldze on 2017/6/16.
 */

public class ViewAdapter {

    //防重复点击间隔(秒)
    public static final int CLICK_INTERVAL = 1;

    /**
     * requireAll 是意思是是否需要绑定全部参数, false为否
     * View的onClick事件绑定
     * onClickCommand 绑定的命令,
     * isThrottleFirst 是否开启防止过快点击
     */
    @BindingAdapter(value = {"onClickCommand", "isThrottleFirst"}, requireAll = false)
    public static void onClickCommand(View view, final View.OnClickListener listener, final boolean isThrottleFirst) {
        if (isThrottleFirst) {
            RxView.clicks(view)
                    .subscribe(new Consumer<Unit>() {
                        @Override
                        public void accept(Unit unit) throws Exception {
                            if (listener != null) {
                                listener.onClick(view);
                            }
                        }
                    });
        } else {
            RxView.clicks(view)
                    .throttleFirst(CLICK_INTERVAL, TimeUnit.SECONDS)//1秒钟内只允许点击1次
                    .subscribe(new Consumer<Unit>() {
                        @Override
                        public void accept(Unit unit) throws Exception {
                            if (listener != null) {
                                listener.onClick(view);
                            }
                        }
                    });
        }
    }
}
