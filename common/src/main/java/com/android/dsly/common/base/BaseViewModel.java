package com.android.dsly.common.base;

import android.app.Application;

import com.trello.rxlifecycle3.LifecycleProvider;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

/**
 * @author 陈志鹏
 * @date 2019-12-23
 */
public class BaseViewModel<M extends IModel> extends AndroidViewModel {
    protected M model;
    //弱引用持有
    private WeakReference<LifecycleProvider> lifecycle;
    //等待框
    private MutableLiveData<Boolean> dialogEvent;
    //结束activity
    private MutableLiveData<Void> finishEvent;

    public BaseViewModel(@NonNull Application application) {
        this(application, null);
    }

    public BaseViewModel(@NotNull Application application, M model) {
        super(application);
        this.model = model;
    }

    /**
     * 注入RxLifecycle生命周期
     *
     * @param lifecycle
     */
    public void setLifecycleProvider(LifecycleProvider lifecycle) {
        this.lifecycle = new WeakReference<>(lifecycle);
    }

    public LifecycleProvider getLifecycleProvider() {
        return lifecycle.get();
    }

    public MutableLiveData<Boolean> getDialogEvent() {
        if (dialogEvent == null) {
            dialogEvent = new MutableLiveData<>();
        }
        return dialogEvent;
    }

    public MutableLiveData<Void> getFinishEvent() {
        if (finishEvent == null) {
            finishEvent = new MutableLiveData<>();
        }
        return finishEvent;
    }

    public void showDialog(Boolean showDialog) {
        getDialogEvent().setValue(showDialog);
    }

    public void finish() {
        getFinishEvent().setValue(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (lifecycle != null) {
            lifecycle.clear();
        }
        if (model != null){
            model.onCleared();
        }
    }
}
