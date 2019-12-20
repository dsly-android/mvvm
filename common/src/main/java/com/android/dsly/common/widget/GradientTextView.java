package com.android.dsly.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;

import com.android.dsly.common.R;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

/**
 * @author 陈志鹏
 * @date 2019-08-29
 */
public class GradientTextView extends AppCompatTextView {

    //默认是横向
    private boolean mIsVertical;
    private int mStartColor;
    private int mCenterColor;
    private int mEndColor;

    public GradientTextView(Context context) {
        this(context, null);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.GradientTextView);
        mIsVertical = typedArray.getBoolean(R.styleable.GradientTextView_isVertical, false);
        mStartColor = typedArray.getColor(R.styleable.GradientTextView_startColor, ContextCompat.getColor(context, android.R.color.white));
        mCenterColor = typedArray.getColor(R.styleable.GradientTextView_centerColor, 0);
        mEndColor = typedArray.getColor(R.styleable.GradientTextView_endColor, ContextCompat.getColor(context, android.R.color.white));
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //文字的宽度
        int mViewWidth = 0;
        //文字的高度
        int mViewHeight = 0;
        if (mIsVertical) {
            mViewHeight = getMeasuredHeight();
        } else {
            mViewWidth = getMeasuredWidth();
        }
        Paint paint = getPaint();
        String mTipText = getText().toString();
        Rect mTextBound = new Rect();
        paint.getTextBounds(mTipText, 0, mTipText.length(), mTextBound);
        //前面4个参数分别表示渐变的开始x轴,开始y轴,结束的x轴,结束的y轴,mcolorList表示渐变的颜色数组
        LinearGradient linearGradient = new LinearGradient(0, 0, mViewWidth, mViewHeight,
                mCenterColor == 0 ? new int[]{mStartColor, mEndColor} : new int[]{mStartColor, mCenterColor, mEndColor},
                null, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        //画出文字
        canvas.drawText(mTipText, getMeasuredWidth() / 2 - mTextBound.width() / 2, getMeasuredHeight() / 2 + mTextBound.height() / 2, paint);
    }

    /**
     * true表示纵向渐变,false变身横向渐变
     *
     * @param vertical
     */
    public void setVertical(boolean vertical) {
        mIsVertical = vertical;
    }

    /**
     * 设置渐变开始的颜色
     */
    public void setStartColor(int color) {
        mStartColor = color;
    }

    /**
     * 设置渐变中间的颜色
     */
    public void setCenterColor(int centerColor) {
        this.mCenterColor = centerColor;
    }

    /**
     * 设置渐变结束的颜色
     */
    public void setEndColor(int endColor) {
        this.mEndColor = endColor;
    }
}