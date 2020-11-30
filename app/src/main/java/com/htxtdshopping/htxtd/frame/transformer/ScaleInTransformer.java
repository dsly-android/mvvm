package com.htxtdshopping.htxtd.frame.transformer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

/**
 * @author 陈志鹏
 * @date 2020/11/29
 */
public class ScaleInTransformer implements ViewPager2.PageTransformer {
    public static final float DEFAULT_MIN_SCALE = 0.85f;
    public static final float DEFAULT_CENTER = 0.5f;

    private float mMinScale = DEFAULT_MIN_SCALE;

    @Override
    public void transformPage(@NonNull View view, float position) {
        view.setElevation(-Math.abs(position));
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        view.setPivotY(pageHeight / 2);
        view.setPivotX(pageWidth / 2);
        if (position < -1) {
            view.setScaleX(mMinScale);
            view.setScaleY(mMinScale);
        } else if (position <= 1) {
            if (position < 0) {
                float scaleFactor = (1 + position) * (1 - mMinScale) + mMinScale;
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else {
                float scaleFactor = (1 - position) * (1 - mMinScale) + mMinScale;
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            }
        } else {
            view.setScaleX(mMinScale);
            view.setScaleY(mMinScale);
        }
    }
}
