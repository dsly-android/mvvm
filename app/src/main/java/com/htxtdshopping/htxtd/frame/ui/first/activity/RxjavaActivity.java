package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.rxhttp.observer.CommonObserver;
import com.blankj.utilcode.util.LogUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.network.ServerApi;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxjavaActivity extends BaseFitsWindowActivity {

    @BindView(R.id.iv_test1)
    ImageView mIvTest1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_rxjava;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_test1, R.id.btn_test2, R.id.btn_test3, R.id.btn_loading})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_test1:
                test1();
                break;
            case R.id.btn_test2:
                test2();
                break;
            case R.id.btn_test3:
                test3();
                break;
            case R.id.btn_loading:
                test4();
                break;
            default:
                break;
        }
    }

    private void test1() {
        Observable.just("")
                .flatMap(new Function<String, Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> apply(String s) throws Exception {
                        return ServerApi.getBitmap("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2230167403,4188800858&fm=27&gp=0.jpg");
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(new CommonObserver<Bitmap>() {
                    @Override
                    protected void onSuccess(Bitmap bitmap) {
                        mIvTest1.setImageBitmap(bitmap);
                    }
                });
    }

    private void test2() {
        Observable<Long> test1 = Observable.intervalRange(1, 5, 1, 1, TimeUnit.SECONDS);
        Observable<Long> test2 = Observable.intervalRange(11, 5, 1, 1, TimeUnit.SECONDS);
        Observable.mergeDelayError(test1, test2)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long o) throws Exception {
                        LogUtils.e(o);
                    }
                });
    }

    private void test3() {
        Observable<Long> test1 = Observable.intervalRange(1, 5, 1, 1, TimeUnit.SECONDS);
        Observable<Long> test2 = Observable.intervalRange(11, 5, 1, 1, TimeUnit.SECONDS);
        Observable.concatArrayDelayError(test1, test2)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtils.e(aLong + "");
                    }
                });
    }

    private void test4() {
        Observable.just(1, 2, 3)
                .delay(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading();
                    }
                }).doFinally(new Action() {
            @Override
            public void run() throws Exception {
                hideLoading();
            }
        })
                .compose(bindToLifecycle())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtils.e(integer + "");
                    }
                });
    }
}
