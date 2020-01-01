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
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.bean.NewsVideoBean;
import com.htxtdshopping.htxtd.frame.bean.RefreshBean;
import com.htxtdshopping.htxtd.frame.databinding.ActivityRefreshAndLoadMoreBinding;
import com.htxtdshopping.htxtd.frame.ui.first.adapter.RefreshAndLoadMoreAdapter;
import com.htxtdshopping.htxtd.frame.ui.first.viewmodel.RefreshAndLoadMoreViewModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author chenzhipeng
 */
public class RefreshAndLoadMoreActivity extends BaseFitsWindowActivity<ActivityRefreshAndLoadMoreBinding, RefreshAndLoadMoreViewModel> {

    private RefreshAndLoadMoreAdapter mAdapter;
    private TextView mTvRefreshNum;

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
        mTvRefreshNum = findViewById(R.id.tv_refresh_num);

        mBinding.rvContent.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvContent.addItemDecoration(new LinearDividerItemDecoration(this, AutoSizeUtils.pt2px(this, 1)));
        mAdapter = new RefreshAndLoadMoreAdapter();
        mAdapter.getLoadMoreModule().setLoadMoreView(new CustomizeLoadMoreView());
        mBinding.rvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mBinding.nrlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mAdapter.getLoadMoreModule().setEnableLoadMore(false);
                mViewModel.loadData(true);
            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityUtils.startActivity(NewsVideoActivity.class);
            }
        });
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mBinding.nrlRefresh.setEnableRefresh(false);
                mViewModel.loadData(false);
            }
        });
        mBinding.rvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager) mBinding.rvContent.getLayoutManager();
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
        mViewModel.getLiveData().observe(this, new Observer<RefreshBean<List<MultiItemEntity>>>() {
            @Override
            public void onChanged(RefreshBean<List<MultiItemEntity>> response) {
                if (response == null) {
                    mBinding.nrlRefresh.setEnableRefresh(true);
                    mAdapter.getLoadMoreModule().setEnableLoadMore(true);
                    mBinding.nrlRefresh.finishRefresh(false);
                    mAdapter.getLoadMoreModule().loadMoreFail();
                } else {
                    if (response.isRefresh()) {
                        mBinding.nrlRefresh.finishRefresh();
                        mAdapter.getLoadMoreModule().setEnableLoadMore(true);
                    } else {
                        mBinding.nrlRefresh.setEnableRefresh(true);
                    }
                    if (response.isRefresh()) {
                        mAdapter.setNewData(response.getData());
                        mTvRefreshNum.setText(String.format(getString(R.string.refresh_news_num), response.getData().size()));
                    } else {
                        mAdapter.addData(response.getData());
                        if (response.getData().size() < 20) {
                            mAdapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            mAdapter.getLoadMoreModule().loadMoreComplete();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        showLoading();
        mViewModel.loadData(true);
    }

    @Override
    public void showLoading() {
        mBinding.tplView.play();
    }

    @Override
    public void hideLoading() {
        mBinding.tplView.stop();
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
