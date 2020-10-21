package com.htxtdshopping.htxtd.frame.ui.third.viewmodel;

import android.app.Application;

import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.network.BaseResponse;
import com.android.dsly.common.network.DataObserver;
import com.android.dsly.rxhttp.RxHttp;
import com.android.dsly.rxhttp.utils.RxLifecycleUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.htxtdshopping.htxtd.frame.bean.CallBean;
import com.htxtdshopping.htxtd.frame.network.CommonApi;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 陈志鹏
 * @date 2020/6/12
 */
public class VoiceViewModel extends BaseViewModel {

    public VoiceViewModel(@NonNull Application application) {
        super(application);
    }

    private Disposable mCallDis;

    private String mRoomId;
    private String mVoiceUserId;

    private MutableLiveData<CallBean> callLiveData = new MutableLiveData<>();
    private MutableLiveData<Void> hangUpLiveData = new MutableLiveData();

    public void setRoomId(String roomId) {
        this.mRoomId = roomId;
    }

    public String getRoomId() {
        return mRoomId;
    }

    public void setVoiceUserId(String voiceUserId) {
        this.mVoiceUserId = voiceUserId;
    }

    public String getVoiceUserId() {
        return mVoiceUserId;
    }

    public void call(final String toUserId) {
        Observable.interval(0, 5, TimeUnit.SECONDS)
                .flatMap(new Function<Long, Observable<BaseResponse<CallBean>>>() {
                    @Override
                    public Observable<BaseResponse<CallBean>> apply(Long aLong) throws Exception {
                        return RxHttp.createApi(CommonApi.class)
                                .call(toUserId, mRoomId);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.<BaseResponse<CallBean>>bindToLifecycle(getLifecycleProvider()))
                .subscribe(new DataObserver<BaseResponse<CallBean>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mCallDis = d;
                    }

                    @Override
                    protected void onSuccess(BaseResponse<CallBean> response) {
                        CallBean data = response.getData();
                        if (data != null) {
                            mRoomId = data.getRoomId();
                            mVoiceUserId = data.getUserId();

                            callLiveData.postValue(data);
                        }
                    }

                    @Override
                    protected void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        ToastUtils.showLong(errorMsg);
                        finish();
                    }
                });
    }

    public void cancelCall() {
        if (mCallDis != null && !mCallDis.isDisposed()) {
            mCallDis.dispose();
        }
    }

    public void hangUp(String toUserId) {
        RxHttp.createApi(CommonApi.class)
                .hangUp(toUserId, mRoomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.<BaseResponse>bindToLifecycle(getLifecycleProvider()))
                .subscribe(new DataObserver<BaseResponse>() {
                    @Override
                    protected void onSuccess(BaseResponse response) {
                        hangUpLiveData.postValue(null);
                    }

                    @Override
                    protected void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        ToastUtils.showLong(errorMsg);
                        finish();
                    }
                });
    }

    public MutableLiveData<CallBean> getCallLiveData() {
        return callLiveData;
    }

    public MutableLiveData<Void> getHangUpLiveData() {
        return hangUpLiveData;
    }
}
