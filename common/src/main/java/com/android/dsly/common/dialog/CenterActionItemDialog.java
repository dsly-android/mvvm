package com.android.dsly.common.dialog;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.R;
import com.android.dsly.common.adapter.CenterActionItemAdapter;
import com.android.dsly.common.base.BaseDialogFragment;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.databinding.DialogCenterActionItemBinding;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * @author 陈志鹏
 * @date 2021/1/8
 */
public class CenterActionItemDialog extends BaseDialogFragment<DialogCenterActionItemBinding, BaseViewModel> {

    private CenterActionItemAdapter mAdapter;
    private List<String> mDatas;
    private OnItemClickListener mOnItemClickListener;

    public CenterActionItemDialog(List<String> datas, OnItemClickListener listener) {
        mDatas = datas;
        mOnItemClickListener = listener;
    }

    @Override
    public String getDialogTag() {
        return "CenterActionItemDialog";
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_center_action_item;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mAdapter = new CenterActionItemAdapter(mDatas);
        mBinding.rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mAdapter.setOnItemClickListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mDatas.get(position), position);
                }
                dismiss();
            }
        });
    }

    @Override
    public void initData() {

    }

    public static class Builder {

        private List<String> mDatas = new ArrayList<>();

        private OnItemClickListener mOnItemClickListener;

        public CenterActionItemDialog.Builder addActionItem(String content) {
            mDatas.add(content);
            return this;
        }

        public CenterActionItemDialog.Builder  setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
            return this;
        }

        public void show(@NonNull FragmentManager manager) {
            new CenterActionItemDialog(mDatas, mOnItemClickListener).show(manager);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(String content, int pos);
    }
}
