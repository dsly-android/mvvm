package com.android.dsly.common.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.android.dsly.common.R;
import com.android.dsly.common.databinding.PopupActionItemBinding;
import com.android.dsly.common.listener.OnItemAntiClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 陈志鹏 on 2018/4/26.
 */
public abstract class BaseActionItemPopup extends BasePopupWindow<PopupActionItemBinding> {

    private long mAnimDuration = 250;
    private FrameLayout mFlContainer;
    private RecyclerView mRvContent;
    private ActionItemAdapter mAdapter;

    public BaseActionItemPopup(Context context) {
        super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        fitPopupWindowOverStatusBar();

        init();
    }

    public void addActionItem(String title, int drawableId) {
        ActionItem item = new ActionItem(mContext, title, drawableId);
        mAdapter.addData(item);
    }

    public void setGravity(int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mRvContent.getLayoutParams();
        params.gravity = gravity;
        mRvContent.setLayoutParams(params);
    }

    public void setMargin(int left, int top, int right, int bottom) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mRvContent.getLayoutParams();
        params.leftMargin = left;
        params.topMargin = top;
        params.rightMargin = right;
        params.bottomMargin = bottom;
        mRvContent.setLayoutParams(params);
    }

    public void setMarginRight(int right) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mRvContent.getLayoutParams();
        params.rightMargin = right;
        mRvContent.setLayoutParams(params);
    }

    @Override
    public int getLayoutId() {
        return R.layout.popup_action_item;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mFlContainer = mBinding.getRoot().findViewById(R.id.fl_container);
        mRvContent = mBinding.getRoot().findViewById(R.id.rv_content);
        mRvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ActionItemAdapter();
        mRvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mBinding.flContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mAdapter.setOnItemClickListener(new OnItemAntiClickListener() {
            @Override
            public void onItemAntiClick(BaseQuickAdapter adapter, View view, int position) {
                BaseActionItemPopup.this.onItemClick(position);
                dismiss();
            }
        });
    }

    @Override
    public void initData() {

    }

    public void show(View parent) {
        initPosition(parent);

        super.showAtLocation(parent, Gravity.CENTER,0,0);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(mAnimDuration);
        mFlContainer.startAnimation(alphaAnimation);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        scaleAnimation.setDuration(mAnimDuration);
        mRvContent.startAnimation(scaleAnimation);
    }

    /**
     * 初始化弹出框显示的位置
     * @param parent
     */
    public abstract void initPosition(View parent);

    @Override
    public void dismiss() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(mAnimDuration);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                BaseActionItemPopup.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mFlContainer.startAnimation(alphaAnimation);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        scaleAnimation.setDuration(mAnimDuration);
        mRvContent.startAnimation(scaleAnimation);
    }

    private class ActionItemAdapter extends BaseQuickAdapter<ActionItem, BaseViewHolder> {

        public ActionItemAdapter() {
            super(R.layout.item_action_item);
        }

        @Override
        protected void convert(BaseViewHolder helper, ActionItem item) {
            helper.setImageDrawable(R.id.iv_icon, item.mDrawable);
            helper.setText(R.id.tv_title, item.mTitle);
            int position = helper.getLayoutPosition();
            if (position == 0) {
                helper.setBackgroundResource(R.id.fl_parent, R.drawable.bg_action_item_top_selector);
                helper.setVisible(R.id.v_line, true);
            } else if (position == getData().size() - 1) {
                helper.setBackgroundResource(R.id.fl_parent, R.drawable.bg_action_item_bottom_selector);
                helper.setVisible(R.id.v_line, false);
            } else {
                helper.setBackgroundResource(R.id.fl_parent, R.drawable.bg_action_item_center_selector);
                helper.setVisible(R.id.v_line, true);
            }
        }
    }

    private class ActionItem {
        public Drawable mDrawable;
        public String mTitle;

        public ActionItem(Drawable drawable, String title) {
            this.mDrawable = drawable;
            this.mTitle = title;
        }

        public ActionItem(Context context, int titleId, int drawableId) {
            this.mTitle = context.getResources().getString(titleId);
            this.mDrawable = context.getResources().getDrawable(drawableId);
        }

        public ActionItem(Context context, String title, int drawableId) {
            this.mTitle = title;
            this.mDrawable = context.getResources().getDrawable(drawableId);
        }
    }

    public abstract void onItemClick(int position);
}
