package com.htxtdshopping.htxtd.frame.ui.four.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.bean.ChannelBean;
import com.htxtdshopping.htxtd.frame.ui.four.adapter.ChooseChannelAdapter;
import com.htxtdshopping.htxtd.frame.widget.adapter.MultiItemBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class ChooseChannelActivity extends BaseFitsWindowActivity {

    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    private ChooseChannelAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_channel;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mRvContent.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter = new ChooseChannelAdapter();
        mAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int i) {
                if (mAdapter.getData().get(i).getItemType() == ChooseChannelAdapter.TYPE_SECTION) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });
        mRvContent.setAdapter(mAdapter);

        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mRvContent);
        mAdapter.enableDragItem(itemTouchHelper, R.id.tv_select_channel_name, true);

        mAdapter.setItemTouchHelper(itemTouchHelper);
    }

    @Override
    public void initEvent() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int viewType = adapter.getItemViewType(position);
                switch (viewType) {
                    case ChooseChannelAdapter.TYPE_NOT_SELECTED:
                        ChannelBean bean = (ChannelBean) mAdapter.getData().get(position);
                        bean.setItemType(ChooseChannelAdapter.TYPE_SELECTED);
                        mAdapter.notifyItemChanged(position);
                        int from = position;
                        int to = mAdapter.getItemTypeNum(ChooseChannelAdapter.TYPE_SELECTED)
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
            ChannelBean bean = new ChannelBean(ChooseChannelAdapter.TYPE_SELECTED);
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