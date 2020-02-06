package com.htxtdshopping.htxtd.frame.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.htxtdshopping.htxtd.frame.R;

import androidx.core.content.ContextCompat;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author 陈志鹏
 * @date 2019/1/15
 */
public class IndicatorView extends View {
    private int mIndicatorRadius;
    private int mIndicatorMargin;
    private int mPageCount;
    private int mSelectPosition;
    private float mLeftPosition;
    private Paint mSelectPaint;
    private Paint mNormalPaint;

    public IndicatorView(Context var1) {
        this(var1, null);
    }

    public IndicatorView(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public IndicatorView(Context var1, AttributeSet attrs, int var3) {
        super(var1, attrs, var3);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_iv_indicatorMargin, AutoSizeUtils.dp2px(getContext(), 2));
        mIndicatorRadius = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_iv_indicatorRadius, AutoSizeUtils.dp2px(getContext(), 5));
        mPageCount = typedArray.getInt(R.styleable.IndicatorView_iv_pageCount, 0);
        mSelectPosition = typedArray.getInt(R.styleable.IndicatorView_iv_selectPosition, 0);
        int normalColor = typedArray.getColor(R.styleable.IndicatorView_iv_normalColor, ContextCompat.getColor(getContext(), R.color._ffe0e0e0));
        int selectColor = typedArray.getColor(R.styleable.IndicatorView_iv_selectColor, ContextCompat.getColor(getContext(), R.color._999999));
        typedArray.recycle();

        setIndicatorColor(normalColor, selectColor);
    }

    @Override
    protected void onMeasure(int var1, int var2) {
        this.setMeasuredDimension(this.measureWidth(var1), this.measureHeight(var2));
    }

    private int measureWidth(int var1) {
        int var2 = MeasureSpec.getMode(var1);
        int var3 = MeasureSpec.getSize(var1);
        int var5 = this.getPaddingLeft() + this.getPaddingRight() + this.mIndicatorRadius * this.mPageCount * 2 + this.mIndicatorMargin * (this.mPageCount - 1);
        this.mLeftPosition = (float) (this.getMeasuredWidth() - var5) / 2.0F + (float) this.getPaddingLeft();
        int var4;
        if (var2 == 1073741824) {
            var4 = Math.max(var5, var3);
        } else if (var2 == -2147483648) {
            var4 = Math.min(var5, var3);
        } else {
            var4 = var5;
        }

        return var4;
    }

    private int measureHeight(int var1) {
        int var2 = MeasureSpec.getMode(var1);
        int var3 = MeasureSpec.getSize(var1);
        int var4;
        if (var2 == 1073741824) {
            var4 = var3;
        } else {
            int var5 = this.getPaddingTop() + this.getPaddingBottom() + this.mIndicatorRadius * 2;
            if (var2 == -2147483648) {
                var4 = Math.min(var5, var3);
            } else {
                var4 = var5;
            }
        }

        return var4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mSelectPaint != null && this.mNormalPaint != null) {
            float var2 = this.mLeftPosition;
            var2 += (float) this.mIndicatorRadius;

            for (int var3 = 0; var3 < this.mPageCount; ++var3) {
                canvas.drawCircle(var2, (float) this.mIndicatorRadius, (float) this.mIndicatorRadius, var3 == this.mSelectPosition ? this.mSelectPaint : this.mNormalPaint);
                var2 += (float) (this.mIndicatorMargin + this.mIndicatorRadius * 2);
            }
        }
    }

    public void setSelectedPosition(int selectedPosition) {
        this.mSelectPosition = selectedPosition;
        this.invalidate();
    }

    public void setPageCount(int pageCount) {
        this.mPageCount = pageCount;
        this.invalidate();
    }

    public void setIndicator(int indicatorRadius, int indicatorMargin) {
        this.mIndicatorMargin = AutoSizeUtils.dp2px(getContext(), indicatorMargin);
        this.mIndicatorRadius = AutoSizeUtils.dp2px(getContext(), indicatorRadius);
    }

    public void setIndicatorColor(int nornalColor, int selectColor) {
        this.mSelectPaint = new Paint();
        this.mSelectPaint.setStyle(Paint.Style.FILL);
        this.mSelectPaint.setAntiAlias(true);
        this.mSelectPaint.setColor(selectColor);
        this.mNormalPaint = new Paint();
        this.mNormalPaint.setStyle(Paint.Style.FILL);
        this.mNormalPaint.setAntiAlias(true);
        this.mNormalPaint.setColor(nornalColor);
    }
}