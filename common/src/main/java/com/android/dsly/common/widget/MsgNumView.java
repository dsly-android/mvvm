package com.android.dsly.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;

import com.android.dsly.common.R;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author 陈志鹏
 * @date 2019/1/14
 */
public class MsgNumView extends AppCompatTextView {

    private int mNumber;
    private int mBackgroundColor;
    private int mPaddingLeftAndRight;

    public MsgNumView(Context context) {
        this(context, null);
    }

    public MsgNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MsgNumView);
        mNumber = typedArray.getInteger(R.styleable.MsgNumView_mnv_number, 0);
        mBackgroundColor = typedArray.getColor(R.styleable.MsgNumView_mnv_backgroundColor, ContextCompat.getColor(getContext(), android.R.color.holo_red_light));
        mPaddingLeftAndRight = typedArray.getDimensionPixelSize(R.styleable.MsgNumView_mnv_paddingLeftAndRight, AutoSizeUtils.pt2px(getContext(), 5));
        typedArray.recycle();

        setPadding(mPaddingLeftAndRight, 0, mPaddingLeftAndRight, 0);
        showNumber(mNumber);
    }

    public void setBgColor(int color) {
        mBackgroundColor = color;
        setBackground();
    }

    public void showNumber(int number) {
        mNumber = number;
        if (mNumber <= 0) {
            setVisibility(GONE);
        } else if (mNumber > 99) {
            setText("99+");
            setVisibility(VISIBLE);
        } else {
            setText(mNumber + "");
            setVisibility(VISIBLE);
        }
        setBackground();
    }

    private void setBackground() {
        float textSize = getTextSize();
        int height = (int) (textSize + AutoSizeUtils.pt2px(getContext(), 10));
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(height / 2);
        drawable.setColor(mBackgroundColor);
        drawable.setSize(height, height);
        setBackground(drawable);
    }
}
