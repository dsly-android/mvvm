package com.htxtdshopping.htxtd.frame.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.htxtdshopping.htxtd.frame.R;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * 将图片中宽高较长的一端按比例缩放到固定大小
 *
 * @author 陈志鹏
 * @date 2021/2/2
 */
public class FixedImageView extends AppCompatImageView {

    //宽高的最大长度
    private int mMaxLength;

    public FixedImageView(Context context) {
        super(context);
    }

    public FixedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!this.isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FixedImageView);
            mMaxLength = (int) a.getDimension(R.styleable.FixedImageView_fiv_max_length, 0);
            a.recycle();
        }
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        if (drawable != null){
            this.setLayoutParam(drawable);
        }
        super.setImageDrawable(drawable);
    }

    private void setLayoutParam(Drawable drawable) {
        this.measureLayoutParams(this, drawable);
    }

    private void measureLayoutParams(View view, Drawable drawable) {
        int width = drawable.getMinimumWidth();
        int height = drawable.getMinimumHeight();
        if (mMaxLength > 0) {
            int finalWidth;
            int finalHeight;
            if (width < mMaxLength && height < mMaxLength) {
                if (width > height) {
                    finalWidth = mMaxLength;
                    finalHeight = (int) ((float) mMaxLength * 1.0F / (float) width * (float) height);
                } else {
                    finalHeight = mMaxLength;
                    finalWidth = (int) ((float) mMaxLength * 1.0F / (float) height * (float) width);
                }
            } else if (width > height) {
                finalWidth = mMaxLength;
                finalHeight = (int) ((float) mMaxLength * 1.0F / (float) width * (float) height);
            } else {
                finalHeight = mMaxLength;
                finalWidth = (int) ((float) mMaxLength * 1.0F / (float) height * (float) width);
            }
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = finalHeight;
            params.width = finalWidth;
            view.setLayoutParams(params);
        }
    }
}