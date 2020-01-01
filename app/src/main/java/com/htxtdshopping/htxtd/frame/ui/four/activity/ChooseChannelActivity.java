package com.htxtdshopping.htxtd.frame.ui.four.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.GridSpanSizeLookup;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.bean.ChannelBean;
import com.htxtdshopping.htxtd.frame.databinding.ActivityChooseChannelBinding;
import com.htxtdshopping.htxtd.frame.ui.four.adapter.ChooseChannelAdapter;
import com.htxtdshopping.htxtd.frame.widget.adapter.MultiItemBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;

import static com.htxtdshopping.htxtd.frame.ui.four.adapter.ChooseChannelAdapter.TYPE_SELECTED;

public class ChooseChannelActivity extends BaseFitsWindowActivity<ActivityChooseChannelBinding, BaseViewModel> {

    private ChooseChannelAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_channel;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.rvContent.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter = new ChooseChannelAdapter();
        mAdapter.setGridSpanSizeLookup(new GridSpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int viewType, int position) {
                if (mAdapter.getData().get(position).getItemType() == ChooseChannelAdapter.TYPE_SECTION) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });
        mBinding.rvContent.setAdapter(mAdapter);

        mAdapter.getDraggableModule().setDragEnabled(true);
        //设置R.id.tv_select_channel_name响应长按或拖拽
        mAdapter.getDraggableModule().setToggleViewId(R.id.tv_select_channel_name);
        //false：拖拽  true：长按
        mAdapter.getDraggableModule().setDragOnLongPressEnabled(false);
    }

    @Override
    public void initEvent() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int viewType = adapter.getItemViewType(position);
                switch (viewType) {
                    case ChooseChannelAdapter.TYPE_NOT_SELECTED:
                        ChannelBean bean = (ChannelBean) mAdapter.getData().get(position);
                        bean.setItemType(TYPE_SELECTED);
                        mAdapter.notifyItemChanged(position);
                        int from = position;
                        int to = mAdapter.getItemTypeNum(TYPE_SELECTED)
                                + mAdapter.getItemTypeNum(ChooseChannelAdapter.TYPE_SELECTED_FIXED);
                        if (mAdapter.inRange(position) && mAdapter.inRange(to)) {
                            if (from < to) {
                                for (int i = from; i < to; i++) {
                                    Collections.swap(mAdapter.getData(), i, i + 1);
                                }
                            } else {
                                for (int i = from; i > to; i--) {
                                    Collections.swap(mAdapter.getData(), i, i - 1);
                                }
                            }
                            mAdapter.notifyItemMoved(from, to);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        List<MultiItemEntity> datas = new ArrayList<>();
        datas.add(new MultiItemBean(ChooseChannelAdapter.TYPE_SECTION));
        for (int i = 0; i < 13; i++) {
            ChannelBean bean = new ChannelBean(TYPE_SELECTED);
            bean.setChannelId(i);
            bean.setChannelName("title" + i);
            if (i == 0 || i == 1) {
                bean.setItemType(ChooseChannelAdapter.TYPE_SELECTED_FIXED);
            }
            datas.add(bean);
        }
        datas.add(new MultiItemBean(ChooseChannelAdapter.TYPE_SECTION));
        for (int i = 0; i < 9; i++) {
            ChannelBean bean = new ChannelBean(ChooseChannelAdapter.TYPE_NOT_SELECTED);
            bean.setChannelName("new" + i);
            bean.setChannelId(20 + i);
            datas.add(bean);
        }
        mAdapter.setNewData(datas);
    }
}