package com.android.dsly.workmanager;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.constant.RouterHub;
import com.android.dsly.common.utils.ToastUtils;
import com.android.dsly.workmanager.databinding.WorkActivityWorkManagerBinding;

import java.util.concurrent.TimeUnit;

import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import me.jessyan.autosize.utils.LogUtils;

@Route(path = RouterHub.WORK_WORK_MANAGER_ACTIVITY)
public class WorkManagerActivity extends BaseActivity<WorkActivityWorkManagerBinding, BaseViewModel> implements View.OnClickListener {

    private OneTimeWorkRequest oneTimeWorkRequest;
    private PeriodicWorkRequest periodicWorkRequest;

    @Override
    public int getLayoutId() {
        return R.layout.work_activity_work_manager;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.setActivity(this);
    }

    @Override
    public void initEvent() {
        //取消使用特定标记的所有任务
//        WorkManager.getInstance(this).cancelAllWorkByTag("test");
        //返回 LiveData 和具有该标记的所有任务的状态列表
//        WorkManager.getInstance(this).getWorkInfosByTagLiveData("test");
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_single) {
            Constraints constraints = new Constraints.Builder()
                    //仅在设备空闲运行
                    .setRequiresDeviceIdle(true)
                    //仅在接通电源时运行
                    .setRequiresCharging(true)
                    .build();
            //要传递的数据
            Data data = new Data.Builder()
                    .putString("request", "request")
                    .build();
            oneTimeWorkRequest = new OneTimeWorkRequest.Builder(SingleWorker.class)
//                    .setConstraints(constraints)
                    //退避延迟时间指定重试工作前的最短等待时间
//                    .setBackoffCriteria(
//                            BackoffPolicy.LINEAR,
//                            OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
//                            TimeUnit.MILLISECONDS)
                    //延时10秒执行任务
                    .setInitialDelay(10, TimeUnit.SECONDS)
                    //参数数据
                    .setInputData(data)
                    //添加标记
                    .addTag("test")
                    .build();
            WorkManager.getInstance(this).enqueue(oneTimeWorkRequest);

            //添加状态监听
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                LogUtils.e(workInfo.getOutputData().getString("result"));
                            }
                        }
                    });
        } else if (id == R.id.btn_cycle) {
            //要传递的数据
            Data data = new Data.Builder()
                    .putString("request", "request")
                    .build();

            periodicWorkRequest = new PeriodicWorkRequest.Builder(SingleWorker.class, 30, TimeUnit.MINUTES)
                    .setInputData(data)
                    .build();
            WorkManager.getInstance(this).enqueue(periodicWorkRequest);

            //添加状态监听
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(periodicWorkRequest.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                ToastUtils.showLong("cycle");
                            }
                        }
                    });
        }
    }
}