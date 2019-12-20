package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.decoration.LinearDividerItemDecoration;
import com.android.dsly.common.widget.CustomizeLoadMoreView;
import com.blankj.utilcode.util.ActivityUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.bean.NewsVideoBean;
import com.htxtdshopping.htxtd.frame.ui.first.adapter.RefreshAndLoadMoreAdapter;
import com.htxtdshopping.htxtd.frame.ui.first.presenter.RefreshAndLoadMorePresenter;
import com.htxtdshopping.htxtd.frame.ui.first.view.IRefreshAndLoadMoreView;
import com.htxtdshopping.htxtd.frame.widget.ThreePointLoadingView;
import com.htxtdshopping.htxtd.frame.widget.refresh.NewsRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author chenzhipeng
 */
public class RefreshAndLoadMoreActivity extends BaseFitsWindowActivity<RefreshAndLoadMorePresenter> implements IRefreshAndLoadMoreView {

    @BindView(R.id.nrl_refresh)
    NewsRefreshLayout mNrlRefresh;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.tpl_view)
    ThreePointLoadingView mTplView;
    @BindView(R.id.tv_refresh_num)
    TextView mTvRefreshNum;
    private RefreshAndLoadMoreAdapter mAdapter;

    @Override
    public int getLayoutId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
        return R.layout.activity_refresh_and_load_more;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.addItemDecoration(new LinearDividerItemDecoration(this, AutoSizeUtils.pt2px(this, 1)));
        mAdapter = new RefreshAndLoadMoreAdapter(this);
        mAdapter.setLoadMoreView(new CustomizeLoadMoreView());
        mRvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mNrlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mAdapter.setEnableLoadMore(false);
                mPresenter.loadData(true);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityUtils.startActivity(NewsVideoActivity.class);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mNrlRefresh.setEnableRefresh(false);
                mPresenter.loadData(false);
            }
        }, mRvContent);
        mRvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager) mRvContent.getLayoutManager();
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    NewsVideoBean bean = (NewsVideoBean) mAdapter.getData().get(position);
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals(bean.getVideoUrl())
                            && (position < firstVisibleItemPosition || position > lastVisibleItemPosition)) {
                        GSYVideoManager.releaseAllVideos();
                        mAdapter.notifyItemChanged(position);
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        showLoading();
        mPresenter.loadData(true);
    }

    @Override
    public void loadDataSuccess(List<MultiItemEntity> datas, boolean isRefresh) {
        if (isRefresh) {
            mNrlRefresh.finishRefresh();
            mAdapter.setEnableLoadMore(true);
        } else {
            mNrlRefresh.setEnableRefresh(true);
        }
        if (isRefresh) {
            mAdapter.setNewData(datas);
            mTvRefreshNum.setText(String.format(getString(R.string.refresh_news_num), datas.size()));
        } else {
            mAdapter.addData(datas);
            if (datas.size() < 20) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreComplete();
            }
        }
    }

    @Override
    public void loadDataFail() {
        mNrlRefresh.setEnableRefresh(true);
        mAdapter.setEnableLoadMore(true);
        mNrlRefresh.finishRefresh(false);
        mAdapter.loadMoreFail();
    }

    @Override
    public void showLoading() {
        mTplView.play();
    }

    @Override
    public void hideLoading() {
        mTplView.stop();
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }
}
