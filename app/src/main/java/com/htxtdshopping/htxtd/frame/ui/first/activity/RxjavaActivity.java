package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.network.DataObserver;
import com.android.dsly.rxhttp.observer.CommonObserver;
import com.android.dsly.rxhttp.utils.TransformerUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityRxjavaBinding;
import com.htxtdshopping.htxtd.frame.network.ServerApi;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxjavaActivity extends BaseActivity<ActivityRxjavaBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_rxjava;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.setActivity(this);

        RxTextView.textChanges(mBinding.etInput)
                //仅在过了一段指定的时间还没发射数据时才发射一个数据
                .debounce(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                //跳过第一个
                .skip(1)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        LogUtils.i(Thread.currentThread().getName() + "   " + charSequence);
                    }
                });
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
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
            case R.id.btn_test4:
                test5();
                break;
            case R.id.btn_loading:
                test4();
                break;
            case R.id.btn_test5:
                test6();
                break;
            case R.id.btn_test6:
                test7();
                break;
            case R.id.btn_test7:
                test8();
                break;
            case R.id.btn_test8:
                test9();
                break;
            case R.id.btn_test9:
                test10();
                break;
            case R.id.btn_test10:
                test11();
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
                        mBinding.ivTest1.setImageBitmap(bitmap);
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
                        LogUtils.e(o + "");
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

    private void test5() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                SystemClock.sleep(100);
                emitter.onNext(2);
                SystemClock.sleep(100);
                emitter.onNext(3);
                SystemClock.sleep(100);
                emitter.onNext(4);
                SystemClock.sleep(100);
                emitter.onNext(5);
                SystemClock.sleep(100);
                emitter.onNext(6);
                SystemClock.sleep(500);
                emitter.onNext(7);
            }
        }).throttleWithTimeout(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtils.e("" + integer);
                    }
                });
    }

    private void test6() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                LogUtils.i("create:" + Thread.currentThread().getName());
                emitter.onNext("aaa");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        LogUtils.i("map:" + Thread.currentThread().getName());
                        return "bbb";
                    }
                }).subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtils.i("accept:" + Thread.currentThread().getName());
                    }
                });
    }

    private void test7() {
        Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                //选取前6个
                .take(6)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtils.i(Thread.currentThread().getName() + "    " + aLong + "");
                    }
                });
    }

    private boolean mIsCached = false;

    private void test8() {
        Observable<String> cacheObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                if (mIsCached) {
                    emitter.onNext("cache");
                }
                emitter.onComplete();
            }
        });
        Observable<String> netObservable = Observable.just("net");
        //返回一个Observable，它发射两个ObservableSource发出的项，一个接一个，不交织.
        Observable.concat(cacheObservable, netObservable)
                .compose(TransformerUtils.pack(this))
                //从串联队列中取出并发送第一个有效事件（next事件）
                .firstElement()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mIsCached = true;
                        LogUtils.i(s);
                    }
                });
    }

    private void test9() {
        List<String> datas = new ArrayList<>();
        datas.add("aaa");
        datas.add("aaa");
        datas.add("aaa");
        datas.add("aaa");

        List<String> strings = Observable.fromIterable(datas)
                .take(3)
                .toList().blockingGet();
        LogUtils.i(strings.toString());
    }

    private void test10() {
        Observable<Integer> just1 = Observable.just(1);
        Observable<Integer> just2 = Observable.just(2);
//        Observable<Integer> just2 = Observable.error(new NullPointerException());
        Observable.zip(just1, just2, new BiFunction<Integer, Integer, Data>() {
            @Override
            public Data apply(Integer integer1, Integer integer2) throws Exception {
                return new Data(integer1, integer2);
            }
        }).compose(TransformerUtils.pack(this))
                .subscribe(new DataObserver<Data>() {
                    @Override
                    protected void onSuccess(Data data) {
                        super.onSuccess(data);
                        LogUtils.i(data.getA() + "   " + data.getB());
                    }

                    @Override
                    protected void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        LogUtils.i("onError");
                    }
                });

    }

    private void test11(){
//        Observable first = Observable.empty();
        Observable first = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                //使用第一个
//                emitter.onNext("first");
//                emitter.onComplete();

                //使用第二个
                emitter.onError(new NullPointerException("null"));
            }
        }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                return Observable.empty();
            }
        });
        //使用第二个
        Observable two = Observable.just("two");

        first.switchIfEmpty(two)
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        ToastUtils.showLong(o.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showLong(throwable.getMessage());
                    }
                });
    }

    class Data {
        private Integer a;
        private Integer b;

        public Data(Integer a, Integer b) {
            this.a = a;
            this.b = b;
        }

        public Integer getA() {
            return a;
        }

        public void setA(Integer a) {
            this.a = a;
        }

        public Integer getB() {
            return b;
        }

        public void setB(Integer b) {
            this.b = b;
        }
    }
}
