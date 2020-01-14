package com.android.dsly.workmanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import me.jessyan.autosize.utils.LogUtils;

/**
 * @author 陈志鹏
 * @date 2020-01-12
 */
public class SingleWorker extends Worker {

    public SingleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        LogUtils.e(Thread.currentThread().getName());
        LogUtils.e(getInputData().getString("request"));
        Data resultData = new Data.Builder()
                .putString("result","result").build();
        return Result.success(resultData);
    }
}
