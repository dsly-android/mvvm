package com.htxtdshopping.htxtd.frame.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htxtdshopping.htxtd.frame.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.internal.ArrowDrawable;
import com.scwang.smartrefresh.layout.internal.ProgressDrawable;

import androidx.annotation.NonNull;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author 陈志鹏
 * @date 2018/10/12
 */
public class NewsRefreshHeader extends FrameLayout implements RefreshHeader {

    private LinearLayout mLlPullDown;
    private ImageView mIvArrow;
    private TextView mTvPrompt;
    private LinearLayout mLlRefreshing;
    private ImageView mIvProgress;
    private TextView mTvFinish;
    private TextView mTvRefreshNum;

    private ProgressDrawable mProgressDrawable;

    public NewsRefreshHeader(Context context) {
        this(context, null);
    }

    public NewsRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        View.inflate(context, R.layout.refresh_news_header, this);
        mLlPullDown = findViewById(R.id.ll_pull_down);
        mIvArrow = findViewById(R.id.iv_arrow);
        mTvPrompt = findViewById(R.id.tv_prompt);
        mLlRefreshing = findViewById(R.id.ll_refreshing);
        mIvProgress = findViewById(R.id.iv_progress);
        mTvFinish = findViewById(R.id.tv_finish);
        mTvRefreshNum = findViewById(R.id.tv_refresh_num);

        mProgressDrawable = new ProgressDrawable();
        mIvProgress.setImageDrawable(mProgressDrawable);
        mIvArrow.setImageDrawable(new ArrowDrawable());

        setMinimumHeight(AutoSizeUtils.pt2px(context, 100));
    }

    @Override
    @NonNull
    public View getView() {
        //真实的视图就是自己，不能返回null
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        //指定为平移，不能null
        return SpinnerStyle.Translate;
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int maxDragHeight) {
        //开始动画
        mProgressDrawable.start();
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        //停止动画
        mProgressDrawable.stop();
        mLlRefreshing.setVisibility(GONE);
        if (success) {
            mTvFinish.setVisibility(GONE);
            mTvRefreshNum.setVisibility(VISIBLE);
        } else {
            mTvFinish.setVisibility(VISIBLE);
            mTvFinish.setText("刷新失败");
            mTvRefreshNum.setVisibility(GONE);
        }
        //延迟500毫秒之后再弹回
        return 500;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                mLlPullDown.setVisibility(VISIBLE);
                mTvPrompt.setText("下拉开始刷新");
                //显示下拉箭头
                mIvArrow.setVisibility(VISIBLE);
                mLlRefreshing.setVisibility(GONE);
                mTvFinish.setVisibility(GONE);
                //隐藏刷新数量
                mTvRefreshNum.setVisibility(GONE);
                //还原箭头方向
                mIvArrow.animate().rotation(0);
                break;
            case Refreshing:
                mLlPullDown.setVisibility(GONE);
                mLlRefreshing.setVisibility(VISIBLE);
                break;
            case ReleaseToRefresh:
                mLlPullDown.setVisibility(VISIBLE);
                mTvPrompt.setText("释放立即刷新");
                //显示箭头改为朝上
                mIvArrow.animate().rotation(180);
                break;
            default:
                break;
        }
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }
}
