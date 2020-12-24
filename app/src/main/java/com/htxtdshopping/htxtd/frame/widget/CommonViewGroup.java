package com.htxtdshopping.htxtd.frame.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;

import androidx.core.content.ContextCompat;

/**
 * @author 陈志鹏
 * @date 2020-01-02
 */
public class CommonViewGroup extends ViewGroup {

    public CommonViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //viewgroup默认不走ondraw方法，设置为false会走
        setWillNotDraw(false);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        LogUtils.e(widthMode + "  " + sizeWidth + "  " + heightMode + "   " + sizeHeight);

        //获取布局中的宽高，不需要调用measure也能获取到
        View childView = getChildAt(0);
        LayoutParams layoutParams = childView.getLayoutParams();
        LogUtils.e(layoutParams.height + "   " + layoutParams.width);

        //调用这个方法后才能在getMeasuredWidth方法中获取到值
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            //必须调用measure**方法才能获取到值
            width = view.getMeasuredWidth() * 2;
            height = view.getMeasuredHeight() * 2;
        }
        LogUtils.e(width + "   " + height);

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? sizeWidth : width,
                heightMode == MeasureSpec.EXACTLY ? sizeHeight : height);
    }

    /**
     * padding部分的ui没有显示出来
     *
     * left、top、right、bottom表示本控件在父控件中的位置
     *
     * @param changed
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        LogUtils.e(BarUtils.getActionBarHeight() + "  " + left + "  " + top + "  " + right + "  " + bottom);
//        getPaddingLeft() : padding部分需要在onLayout中自己判断
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(30);
        paint.setColor(ContextCompat.getColor(getContext(), android.R.color.black));
        canvas.drawText("1111", 350, 350, paint);
    }
}