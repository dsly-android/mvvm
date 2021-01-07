package com.android.dsly.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.android.dsly.common.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author 陈志鹏
 * @date 2019-09-23
 */
public class UnreadMsgView extends FrameLayout {

    private MsgNumView mMsgNumView;
    private View mVDot;

    private int mNumber;
    private int mBackgroundColor;
    private int mPaddingLeftAndRight;
    private int mStyle;

    public UnreadMsgView(@NonNull Context context) {
        this(context, null);
    }

    public UnreadMsgView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.UnreadMsgView);
        mNumber = typedArray.getInteger(R.styleable.UnreadMsgView_umv_number, 0);
        mBackgroundColor = typedArray.getColor(R.styleable.UnreadMsgView_umv_backgroundColor, ContextCompat.getColor(getContext(), android.R.color.holo_red_light));
        mPaddingLeftAndRight = typedArray.getDimensionPixelSize(R.styleable.UnreadMsgView_umv_paddingLeftAndRight, AutoSizeUtils.dp2px(getContext(), 2));
        mStyle = typedArray.getInt(R.styleable.UnreadMsgView_umv_style, 2);
        typedArray.recycle();

        mMsgNumView = new MsgNumView(getContext());
        mMsgNumView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        mMsgNumView.setPadding(mPaddingLeftAndRight, 0, mPaddingLeftAndRight, 0);
        mVDot = new View(getContext());

        addView(mMsgNumView);
        addView(mVDot, new LayoutParams(AutoSizeUtils.dp2px(getContext(), 5), AutoSizeUtils.dp2px(getContext(), 5)));

        if (mStyle == 0) {
            showDot();
        } else if (mStyle == 1) {
            showNumber(mNumber);
        } else {
            setVisibility(GONE);
        }
    }

    public void setBgColor(int color) {
        mBackgroundColor = color;
        setBackground();
    }

    public void showNumber(int number) {
        mNumber = number;
        mStyle = 1;
        setBackground();
    }

    public void showDot() {
        mStyle = 0;
        setBackground();
    }

    public void setGone() {
        mStyle = 2;
        setBackground();
    }

    private void setBackground() {
        if (mStyle == 0) {
            setVisibility(VISIBLE);
            mMsgNumView.setVisibility(GONE);
            mVDot.setVisibility(VISIBLE);
            int height = AutoSizeUtils.dp2px(getContext(), 5);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(height / 2);
            drawable.setColor(mBackgroundColor);
            drawable.setSize(height, height);
            mVDot.setBackground(drawable);
        } else if (mStyle == 1) {
            setVisibility(VISIBLE);
            mVDot.setVisibility(GONE);
            mMsgNumView.setVisibility(VISIBLE);
            mMsgNumView.setBgColor(mBackgroundColor);
            mMsgNumView.showNumber(mNumber);
        } else {
            setVisibility(GONE);
        }
    }

    public void setTextColor(int color) {
        mMsgNumView.setTextColor(color);
    }

    public void setTextSize(int textSize) {
        mMsgNumView.setTextSize(textSize);
    }
}