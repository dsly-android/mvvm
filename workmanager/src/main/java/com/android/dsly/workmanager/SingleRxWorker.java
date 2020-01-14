package com.android.dsly.workmanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import me.jessyan.autosize.utils.LogUtils;

/**
 * @author 陈志鹏
 * @date 2020-01-12
 */
public class SingleRxWorker extends RxWorker {

    public SingleRxWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Single<Result> createWork() {
        return Single.just(1)
                .map(new Function<Integer, Result>() {
                    @Override
                    public Result apply(Integer integer) throws Exception {
                        LogUtils.e(Thread.currentThread().getName());
                        LogUtils.e(getInputData().getString("request"));
                        Data resultData = new Data.Builder()
                                .putString("result","result").build();
                        return Result.success(resultData);
                    }
                });

    }
}
