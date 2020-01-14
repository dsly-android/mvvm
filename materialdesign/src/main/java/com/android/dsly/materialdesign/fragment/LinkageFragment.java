package com.android.dsly.materialdesign.fragment;


import android.os.Bundle;

import com.android.dsly.common.base.BasePageLazyFragment;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.materialdesign.LinkageAdapter;
import com.android.dsly.materialdesign.R;
import com.android.dsly.materialdesign.databinding.DesignFragmentLinkageBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class LinkageFragment extends BasePageLazyFragment<DesignFragmentLinkageBinding, BaseViewModel> {

    private LinkageAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.design_fragment_linkage;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new LinkageAdapter();
        mBinding.rvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add("item" + i);
        }
        mAdapter.setNewData(datas);
    }
}
