package com.android.dsly.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.android.dsly.common.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author 陈志鹏
 * @date 2020/9/8
 */
public class MaxRecyclerView extends RecyclerView {

    private int mMaxWidth = 0;
    private int mMaxHeight = 0;

    public MaxRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public MaxRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.MaxRecyclerView);
        mMaxWidth = arr.getLayoutDimension(R.styleable.MaxRecyclerView_maxWidth, 0);
        mMaxHeight = arr.getLayoutDimension(R.styleable.MaxRecyclerView_maxHeight, 0);
        arr.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxWidth > 0) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.AT_MOST);
        }
        if (mMaxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
