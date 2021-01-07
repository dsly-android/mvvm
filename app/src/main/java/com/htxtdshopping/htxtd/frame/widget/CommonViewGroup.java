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

    /**
     * 布局中使用
     * 根据提供的属性集返回一组新的布局参数。
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /**
     * 代码中使用
     * 根据提供的布局参数返回一组安全的布局参数
     */
    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    /**
     * 代码中使用
     * 返回一组默认布局参数。
     * 当传递给{@link #addView（View）}的视图尚未设置布局参数时，将请求这些参数。
     * 如果返回null，则addView会引发异常。
     */
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * 检查LayoutParams是否合格
     */
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p) && p instanceof LayoutParams;
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
        LayoutParams layoutParams = (LayoutParams) childView.getLayoutParams();
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
     * <p>
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

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            /*TypedArray a =
                    c.obtainStyledAttributes(attrs, R.styleable.LinearLayout_Layout);

            weight = a.getFloat(R.styleable.LinearLayout_Layout_layout_weight, 0);
            gravity = a.getInt(R.styleable.LinearLayout_Layout_layout_gravity, -1);

            a.recycle();*/
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }
}