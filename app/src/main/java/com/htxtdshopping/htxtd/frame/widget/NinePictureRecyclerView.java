package com.htxtdshopping.htxtd.frame.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.android.dsly.common.adapter.BaseBindingAdapter;
import com.android.dsly.common.decoration.GridDividerItemDecoration;
import com.android.dsly.common.utils.GlideUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ItemNinePictureBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 朋友圈九宫格图片显示
 *
 * @author 陈志鹏
 * @date 2021/7/20
 */
public class NinePictureRecyclerView extends RecyclerView {

    private NinePictureAdapter mAdapter;

    public NinePictureRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public NinePictureRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NinePictureRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setLayoutManager(new GridLayoutManager(getContext(), 3));
        addItemDecoration(new GridDividerItemDecoration(getContext(), ConvertUtils.dp2px(4)));
        mAdapter = new NinePictureAdapter();
        setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (hasNullData() && position == 2) {
                    return;
                }
                if (hasNullData() && (position == 3 || position == 4)) {
                    position--;
                }
                showPreviewImage(position);
            }
        });
    }

    /**
     * 显示预览图片
     */
    private void showPreviewImage(int position) {
//        Mojito.with(getContext())
//                .urls(getDatas())
//                .position(position, 0, 0)
//                .views(getViews())
//                .setIndicator(new CircleIndexIndicator())
//                .start();
    }

    private View[] getViews(){
        List<View> views = new ArrayList<>();
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            String s = mAdapter.getData().get(i);
            if (!StringUtils.isEmpty(s)){
                views.add(getChildAt(i));
            }
        }
        return views.toArray(new View[0]);
    }

    public boolean hasNullData() {
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            String s = mAdapter.getData().get(i);
            if (StringUtils.isEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getDatas() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            String s = mAdapter.getData().get(i);
            if (!StringUtils.isEmpty(s)) {
                datas.add(s);
            }
        }
        return datas;
    }

    public void setDatas(ArrayList<String> datas) {
        if (datas.size() == 4) {
            datas.add(2, null);
        }
        mAdapter.setNewData(datas);
    }

    private static class NinePictureAdapter extends BaseBindingAdapter<String, BaseViewHolder> {

        public NinePictureAdapter() {
            super(R.layout.item_nine_picture);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder holder, @org.jetbrains.annotations.Nullable String s) {
            ItemNinePictureBinding binding = holder.getBinding();
            if (!StringUtils.isEmpty(s)) {
                GlideUtils.loadImage(getContext(), s, binding.ivImg);
            }
        }
    }
}
