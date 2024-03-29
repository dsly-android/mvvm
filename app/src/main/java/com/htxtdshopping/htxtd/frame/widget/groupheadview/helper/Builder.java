package com.htxtdshopping.htxtd.frame.widget.groupheadview.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Region;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.android.dsly.common.R;
import com.blankj.utilcode.util.CacheMemoryUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.htxtdshopping.htxtd.frame.widget.groupheadview.layout.DingLayoutManager;
import com.htxtdshopping.htxtd.frame.widget.groupheadview.layout.ILayoutManager;
import com.htxtdshopping.htxtd.frame.widget.groupheadview.layout.WechatLayoutManager;
import com.htxtdshopping.htxtd.frame.widget.groupheadview.listener.OnProgressListener;
import com.htxtdshopping.htxtd.frame.widget.groupheadview.listener.OnSubItemClickListener;
import com.htxtdshopping.htxtd.frame.widget.groupheadview.region.DingRegionManager;
import com.htxtdshopping.htxtd.frame.widget.groupheadview.region.IRegionManager;
import com.htxtdshopping.htxtd.frame.widget.groupheadview.region.WechatRegionManager;

import java.util.Arrays;

import androidx.annotation.ColorInt;


public class Builder {
    public Context context;
    public ImageView imageView;
    public int size; // 最终生成bitmap的尺寸
    public int gap; // 每个小bitmap之间的距离
    public int gapColor; // 间距的颜色
    public int placeholder = R.drawable.ic_launcher_background; // 获取图片失败时的默认图片
    public int count; // 要加载的资源数量
    public int subSize; // 单个bitmap的尺寸
    public int maxDisplayNum = 9;//最大显示数量

    public ILayoutManager layoutManager; // bitmap的组合样式

    public Region[] regions;
    public OnSubItemClickListener subItemClickListener; // 单个bitmap点击事件回调

    public OnProgressListener progressListener; // 最终的组合bitmap回调接口

    public Bitmap[] bitmaps;
    public int[] resourceIds;
    public String[] urls;


    public Builder(Context context) {
        this.context = context;
    }

    public Builder(Context context, int maxDisplayNum) {
        this.context = context;
        this.maxDisplayNum = maxDisplayNum;
    }

    public Builder setImageView(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }

    public Builder setSize(int size) {
        this.size = ConvertUtils.dp2px(size);
        return this;
    }

    public Builder setGap(int gap) {
        this.gap = ConvertUtils.dp2px(gap);
        return this;
    }

    public Builder setGapColor(@ColorInt int gapColor) {
        this.gapColor = gapColor;
        return this;
    }

    public Builder setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public Builder setLayoutManager(ILayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }

    public Builder setOnProgressListener(OnProgressListener progressListener) {
        this.progressListener = progressListener;
        return this;
    }

    public Builder setOnSubItemClickListener(OnSubItemClickListener subItemClickListener) {
        this.subItemClickListener = subItemClickListener;
        return this;
    }

    public Builder setBitmaps(Bitmap... bitmaps) {
        if (bitmaps.length > maxDisplayNum) {
            Bitmap[] _bitmaps = new Bitmap[maxDisplayNum];
            System.arraycopy(bitmaps, 0, _bitmaps, 0, maxDisplayNum);
            this.bitmaps = _bitmaps;
            this.count = maxDisplayNum;
        } else {
            this.bitmaps = bitmaps;
            this.count = bitmaps.length;
        }
        return this;
    }

    public Builder setUrls(String... urls) {
        if (urls.length > maxDisplayNum) {
            String[] _urls = new String[maxDisplayNum];
            System.arraycopy(urls, 0, _urls, 0, maxDisplayNum);
            this.urls = _urls;
            this.count = maxDisplayNum;
        } else {
            this.urls = urls;
            this.count = urls.length;
        }
        return this;
    }

    public Builder setResourceIds(int... resourceIds) {
        if (resourceIds.length > maxDisplayNum) {
            int[] _resourceIds = new int[maxDisplayNum];
            System.arraycopy(resourceIds, 0, _resourceIds, 0, maxDisplayNum);
            this.resourceIds = _resourceIds;
            this.count = maxDisplayNum;
        } else {
            this.resourceIds = resourceIds;
            this.count = resourceIds.length;
        }
        return this;
    }

    public void build() {
        imageView.setTag(Utils.hashKeyFormUrl(Arrays.toString(urls)));
        subSize = getSubSize(size, gap, layoutManager, count);
        initRegions();
        Bitmap bitmap = CacheMemoryUtils.getInstance().get(Utils.hashKeyFormUrl(Arrays.toString(urls)));
        if (bitmap == null) {
            imageView.setImageResource(placeholder);
            CombineHelper.init().load(this);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 根据最终生成bitmap的尺寸，计算单个bitmap尺寸
     *
     * @param size
     * @param gap
     * @param layoutManager
     * @param count
     * @return
     */
    private int getSubSize(int size, int gap, ILayoutManager layoutManager, int count) {
        int subSize = 0;
        if (layoutManager instanceof DingLayoutManager) {
            subSize = size;
        } else if (layoutManager instanceof WechatLayoutManager) {
            if (count < 2) {
                subSize = size;
            } else if (count < 5) {
                subSize = (size - 3 * gap) / 2;
            } else if (count < 10) {
                subSize = (size - 4 * gap) / 3;
            }
        } else {
            throw new IllegalArgumentException("Must use DingLayoutManager or WechatRegionManager!");
        }
        return subSize;
    }

    /**
     * 初始化RegionManager
     */
    private void initRegions() {
        if (subItemClickListener == null || imageView == null) {
            return;
        }

        IRegionManager regionManager;

        if (layoutManager instanceof DingLayoutManager) {
            regionManager = new DingRegionManager();
        } else if (layoutManager instanceof WechatLayoutManager) {
            regionManager = new WechatRegionManager();
        } else {
            throw new IllegalArgumentException("Must use DingLayoutManager or WechatRegionManager!");
        }

        regions = regionManager.calculateRegion(size, subSize, gap, count);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            int initIndex = -1;
            int currentIndex = -1;
            Point point = new Point();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                point.set((int) event.getX(), (int) event.getY());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initIndex = getRegionIndex(point.x, point.y);
                        currentIndex = initIndex;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        currentIndex = getRegionIndex(point.x, point.y);
                        break;
                    case MotionEvent.ACTION_UP:
                        currentIndex = getRegionIndex(point.x, point.y);
                        if (currentIndex != -1 && currentIndex == initIndex) {
                            subItemClickListener.onSubItemClick(currentIndex);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        initIndex = currentIndex = -1;
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 根据触摸点计算对应的Region索引
     *
     * @param x
     * @param y
     * @return
     */
    private int getRegionIndex(int x, int y) {
        for (int i = 0; i < regions.length; i++) {
            if (regions[i].contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

}
