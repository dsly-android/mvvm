package com.htxtdshopping.htxtd.frame.ui.center.activity;

import android.os.Bundle;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityImageBinding;
import com.htxtdshopping.htxtd.frame.ui.center.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

public class ImageActivity extends BaseActivity<ActivityImageBinding, BaseViewModel> {

    private ImageAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_image;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mAdapter = new ImageAdapter();
        mBinding.rvContent.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add("https://img2.baidu.com/it/u=1070003001,653753576&fm=26&fmt=auto&gp=0.jpg");
            datas.add("https://img2.baidu.com/it/u=2116882029,1761299726&fm=26&fmt=auto&gp=0.jpg");
            datas.add("https://img0.baidu.com/it/u=1242053365,2901037121&fm=26&fmt=auto&gp=0.jpg");
            datas.add("https://img0.baidu.com/it/u=3026497175,2751284156&fm=26&fmt=auto&gp=0.jpg");
            datas.add("https://img2.baidu.com/it/u=4247656867,4135832390&fm=11&fmt=auto&gp=0.jpg");
            datas.add("https://img2.baidu.com/it/u=2078631362,2448815467&fm=26&fmt=auto&gp=0.jpg");
            datas.add("https://img0.baidu.com/it/u=3844939064,2416681391&fm=26&fmt=auto&gp=0.jpg");
            datas.add("https://img2.baidu.com/it/u=2124726749,2172282802&fm=26&fmt=auto&gp=0.jpg");
            datas.add("https://img1.baidu.com/it/u=1796587774,1692469122&fm=26&fmt=auto&gp=0.jpg");
            datas.add("https://img1.baidu.com/it/u=3886212450,854269223&fm=26&fmt=auto&gp=0.jpg");
        }
        mAdapter.setNewData(datas);
    }
}