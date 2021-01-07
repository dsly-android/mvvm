package com.android.dsly.common.widget.pagerbottomtabstrip.item;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.dsly.common.R;
import com.android.dsly.common.widget.UnreadMsgView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * @author 陈志鹏
 * @date 2019-09-24
 */
public class SpecialTabItemView extends BaseTabItem {

    private ImageView mIcon;
    private TextView mTitle;
    private UnreadMsgView mMessages;

    private Drawable mDefaultDrawable;
    private Drawable mCheckedDrawable;

    private int mDefaultTextColor = 0x56000000;
    private int mCheckedTextColor = 0x56000000;

    private boolean mChecked;

    public SpecialTabItemView(@NonNull Context context) {
        this(context,null);
    }

    public SpecialTabItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.special_tab, this, true);

        mIcon = findViewById(R.id.icon);
        mTitle = findViewById(R.id.title);
        mMessages = findViewById(R.id.messages);
    }

    /**
     * 方便初始化的方法
     *
     * @param drawableRes        默认状态的图标
     * @param checkedDrawableRes 选中状态的图标
     * @param title              标题
     */
    public void initialize(@DrawableRes int drawableRes, @DrawableRes int checkedDrawableRes, String title) {
        mDefaultDrawable = ContextCompat.getDrawable(getContext(), drawableRes);
        mCheckedDrawable = ContextCompat.getDrawable(getContext(), checkedDrawableRes);
        if (TextUtils.isEmpty(title)){
            mTitle.setVisibility(GONE);
        }else{
            mTitle.setVisibility(VISIBLE);
            mTitle.setText(title);
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        View view = getChildAt(0);
        if (view != null) {
            view.setOnClickListener(l);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked) {
            mIcon.setImageDrawable(mCheckedDrawable);
            mTitle.setTextColor(mCheckedTextColor);
        } else {
            mIcon.setImageDrawable(mDefaultDrawable);
            mTitle.setTextColor(mDefaultTextColor);
        }
        mChecked = checked;
    }

    @Override
    public void setMessageNumber(int number) {
        mMessages.showNumber(number);
    }

    @Override
    public void setHasMessage(boolean hasMessage) {
        mMessages.showDot();
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setDefaultDrawable(Drawable drawable) {
        mDefaultDrawable = drawable;
        if (!mChecked) {
            mIcon.setImageDrawable(drawable);
        }
    }

    @Override
    public void setSelectedDrawable(Drawable drawable) {
        mCheckedDrawable = drawable;
        if (mChecked) {
            mIcon.setImageDrawable(drawable);
        }
    }

    @Override
    public String getTitle() {
        return mTitle.getText().toString();
    }

    public void setTextDefaultColor(@ColorInt int color) {
        mDefaultTextColor = color;
    }

    public void setTextCheckedColor(@ColorInt int color) {
        mCheckedTextColor = color;
    }

    /**
     * 设置消息圆形的颜色
     */
    public void setMessageBackgroundColor(@ColorInt int color) {
        mMessages.setBgColor(color);
    }

    /**
     * 设置消息数字的颜色
     */
    public void setMessageNumberColor(int color) {
        mMessages.setTextColor(color);
    }

    /**
     * 设置文字的大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        mTitle.setTextSize(textSize);
    }

    /**
     * 设置未读数量文字大小
     */
    public void setUnreadMsgTextSize(int textSize) {
        mMessages.setTextSize(textSize);
    }
}
