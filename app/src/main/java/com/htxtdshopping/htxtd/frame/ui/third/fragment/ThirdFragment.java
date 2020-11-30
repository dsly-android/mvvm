package com.htxtdshopping.htxtd.frame.ui.third.fragment;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.dsly.common.base.BaseLazyFragment;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.constant.RouterHub;
import com.android.dsly.common.utils.ToastUtils;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.base.AppContext;
import com.htxtdshopping.htxtd.frame.databinding.FragmentThirdBinding;
import com.htxtdshopping.htxtd.frame.ui.third.activity.ChangeAvatarActivity;
import com.htxtdshopping.htxtd.frame.ui.third.activity.GridActivity;
import com.htxtdshopping.htxtd.frame.ui.third.activity.HandlerActivity;
import com.htxtdshopping.htxtd.frame.ui.third.activity.LifecycleActivity;
import com.htxtdshopping.htxtd.frame.ui.third.activity.ListActivity;
import com.htxtdshopping.htxtd.frame.ui.third.activity.LoginActivity;
import com.htxtdshopping.htxtd.frame.ui.third.activity.NotificationActivity;
import com.htxtdshopping.htxtd.frame.ui.third.activity.PopupWindowActivity;
import com.htxtdshopping.htxtd.frame.ui.third.activity.RandomAccessFileActivity;
import com.htxtdshopping.htxtd.frame.ui.third.activity.SensorActivity;
import com.htxtdshopping.htxtd.frame.ui.third.activity.ServiceActivity;
import com.htxtdshopping.htxtd.frame.ui.third.activity.ViewPager2Activity;
import com.htxtdshopping.htxtd.frame.ui.third.activity.VoiceActivity;
import com.htxtdshopping.htxtd.frame.ui.third.activity.VoiceRecordActivity;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * @author 陈志鹏
 * @date 2018/9/7
 */
public class ThirdFragment extends BaseLazyFragment<FragmentThirdBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_third;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(mBinding.vBar, getResources().getColor(R.color._81D8CF));
    }

    @Override
    public void initEvent() {
        mBinding.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_grid:
                ActivityUtils.startActivity(GridActivity.class);
                break;
            case R.id.btn_list:
                ActivityUtils.startActivity(ListActivity.class);
                break;
            case R.id.btn_record:
                requestPermission();
                break;
            case R.id.btn_popup:
                ActivityUtils.startActivity(PopupWindowActivity.class);
                break;
            case R.id.btn_image_picker:
                ActivityUtils.startActivity(ChangeAvatarActivity.class);
                break;
            case R.id.btn_notification:
                ActivityUtils.startActivity(NotificationActivity.class);
                break;
            case R.id.btn_service:
                ActivityUtils.startActivity(ServiceActivity.class);
                break;
            case R.id.btn_login:
                ActivityUtils.startActivity(LoginActivity.class);
                break;
            case R.id.btn_design:
                ARouter.getInstance().build(RouterHub.DESIGN_MATERIAL_DESIGN_ACTIVITY).navigation();
                break;
            case R.id.btn_version_update:
                AppContext.getInstance().checkUpdate(true);
                break;
            case R.id.btn_lifecycle:
                ActivityUtils.startActivity(LifecycleActivity.class);
                break;
            case R.id.btn_handler:
                ActivityUtils.startActivity(HandlerActivity.class);
                break;
            case R.id.btn_workmanager:
                ARouter.getInstance().build(RouterHub.WORK_WORK_MANAGER_ACTIVITY).navigation();
                break;
            case R.id.btn_voice:
                ActivityUtils.startActivity(VoiceActivity.class);
                break;
            case R.id.btn_randomAccessFile:
                ActivityUtils.startActivity(RandomAccessFileActivity.class);
                break;
            case R.id.btn_sensor:
                ActivityUtils.startActivity(SensorActivity.class);
                break;
            case R.id.btn_viewpager2:
                ActivityUtils.startActivity(ViewPager2Activity.class);
                break;
            default:
                break;
        }
    }

    private void requestPermission() {
        new RxPermissions(this)
                .requestEach(Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            ActivityUtils.startActivity(VoiceRecordActivity.class);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            ToastUtils.showLong("shouldShowRequestPermissionRationale");
                        } else {
                            ToastUtils.showLong("请到设置中开启相机权限");
                        }
                    }
                });
    }
}
