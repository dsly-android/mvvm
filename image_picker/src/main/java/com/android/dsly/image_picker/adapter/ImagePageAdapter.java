package com.android.dsly.image_picker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.android.dsly.common.utils.GlideUtils;
import com.android.dsly.image_picker.local_data.ImageItem;
import com.android.dsly.image_picker.widget.photoview.PhotoView;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;

/**
 * @author 陈志鹏
 * @date 2018/12/14
 */
public class ImagePageAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<ImageItem> mImageItems;
    private OnItemClickListener mListener;

    public ImagePageAdapter(Context context, ArrayList<ImageItem> imageItems) {
        mContext = context;
        this.mImageItems = imageItems;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageItem imageItem = mImageItems.get(position);
        PhotoView photoView = new PhotoView(mContext);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });
        GlideUtils.loadImage(mContext, imageItem.path, photoView);
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public int getCount() {
        return mImageItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
