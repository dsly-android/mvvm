package com.htxtdshopping.htxtd.frame.ui.first.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.bean.NewsPictureBean;
import com.htxtdshopping.htxtd.frame.bean.NewsTextBean;
import com.htxtdshopping.htxtd.frame.bean.NewsVideoBean;
import com.htxtdshopping.htxtd.frame.databinding.ItemNewsPictureBinding;
import com.htxtdshopping.htxtd.frame.databinding.ItemNewsTextBinding;
import com.htxtdshopping.htxtd.frame.widget.CoverVideoPlayer;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.databinding.DataBindingUtil;

/**
 * @author 陈志鹏
 * @date 2018/10/12
 */
public class RefreshAndLoadMoreAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> implements LoadMoreModule {

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_PICTURE = 2;
    public static final int TYPE_VIDEO = 3;

    public RefreshAndLoadMoreAdapter() {
        super(new ArrayList<>());
        addItemType(TYPE_TEXT, R.layout.item_news_text);
        addItemType(TYPE_PICTURE, R.layout.item_news_picture);
        addItemType(TYPE_VIDEO, R.layout.item_news_video);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        int position = helper.getLayoutPosition();
        int itemViewType = helper.getItemViewType();
        switch (itemViewType) {
            case TYPE_TEXT:
                ItemNewsTextBinding itemNewsTextBinding = helper.<ItemNewsTextBinding>getBinding();
                NewsTextBean newsTextBean = (NewsTextBean) item;
                itemNewsTextBinding.tvTitle.setText(newsTextBean.getTitle());
                break;
            case TYPE_PICTURE:
                ItemNewsPictureBinding itemNewsPictureBinding = helper.<ItemNewsPictureBinding>getBinding();
                NewsPictureBean newsPictureBean = (NewsPictureBean) item;
                itemNewsPictureBinding.setData(newsPictureBean);
                break;
            case TYPE_VIDEO:
                NewsVideoBean newsVideoBean = (NewsVideoBean) item;
                helper.setText(R.id.tv_title, newsVideoBean.getTitle());
                CoverVideoPlayer player = helper.getView(R.id.cvp_video);
                player.setPlayTag(newsVideoBean.getVideoUrl());
                player.setPlayPosition(position);
                boolean isPlaying = player.getCurrentPlayer().isInPlayingState();

                if (!isPlaying) {
                    player.setUpLazy(newsVideoBean.getVideoUrl(), false, null, null, newsVideoBean.getTitle());
                }

                //title
                player.getTitleTextView().setVisibility(View.GONE);

                //设置返回键
                player.getBackButton().setVisibility(View.GONE);

                //设置全屏按键功能
                player.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        player.startWindowFullscreen(getContext(), false, true);
                    }
                });
                player.setRotateViewAuto(false);
                player.setLockLand(true);
                player.setReleaseWhenLossAudio(false);
                player.setShowFullAnimation(false);
                player.setIsTouchWiget(false);

                player.setNeedLockFull(true);

                player.loadCoverImage(newsVideoBean.getVideoUrl(), R.mipmap.ic_launcher);

                player.setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        player.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }
}