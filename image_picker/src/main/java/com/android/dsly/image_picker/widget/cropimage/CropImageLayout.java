package com.android.dsly.image_picker.widget.cropimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.android.dsly.image_picker.R;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * 裁剪头像整个layout
 *
 * @author chenzhipeng
 */
public class CropImageLayout extends RelativeLayout {

    private CropZoomImageView mZoomImageView;

    /**
     * 剪裁框距离左右的间距
     */
    private int mHorizontalPadding;

    /**
     * 剪裁框的颜色
     */
    private int mBorderColor;

    /**
     * 剪裁框的宽度
     */
    private int mBorderWidth;

    public CropImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageCropImageLayout);
        mHorizontalPadding = typedArray.getDimensionPixelSize(R.styleable.ImageCropImageLayout_image_horizontalPadding, AutoSizeUtils.pt2px(context, 40));
        mBorderColor = typedArray.getColor(R.styleable.ImageCropImageLayout_image_borderColor, Color.WHITE);
        mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.ImageCropImageLayout_image_borderWidth, AutoSizeUtils.pt2px(context, 2));
        typedArray.recycle();
    }

    public void init(Context context, Bitmap bitmap) {
        mZoomImageView = new CropZoomImageView(context);
        CropImageBorderView mClipImageView = new CropImageBorderView(context);

        android.view.ViewGroup.LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        mZoomImageView.setImageBitmap(bitmap);

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);

        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setBorderColor(mBorderColor);
        mClipImageView.setBorderWidth(mBorderWidth);
    }

    /**
     * 对外公布设置边距的方法,单位为dp
     *
     * @param mHorizontalPadding
     */
    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = AutoSizeUtils.pt2px(getContext(), mHorizontalPadding);
    }

    /**
     * 裁切图片
     *
     * @return
     */
    public Bitmap crop() {
        return mZoomImageView.crop();
    }

}
