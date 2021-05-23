package com.android.dsly.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.dsly.common.R;
import com.blankj.utilcode.util.BarUtils;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author 陈志鹏
 * @date 2019/1/15
 */
public class TitleBar extends LinearLayout implements View.OnClickListener {

    private View mVBar;
    private TextView mTvBack;
    private TextView mTvTitle;
    private ImageView mIvRight;
    private TextView mTvRight;
    private String mTitle;
    private boolean mIsShowBar;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
        initView();
        initEvent();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBar);
        mTitle = typedArray.getString(R.styleable.TitleBar_tb_title);
        mIsShowBar = typedArray.getBoolean(R.styleable.TitleBar_is_show_bar,false);
        typedArray.recycle();
    }

    private void init() {
        setClickable(true);
        setBackgroundResource(R.color._81D8CF);
    }

    private void initView() {
        setOrientation(VERTICAL);

        View.inflate(getContext(), R.layout.view_title, this);
        mVBar = findViewById(R.id.v_bar);
        mTvBack = findViewById(R.id.tv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mIvRight = findViewById(R.id.iv_right);
        mTvRight = findViewById(R.id.tv_right);

        if (mIsShowBar){
            ViewGroup.LayoutParams params = mVBar.getLayoutParams();
            params.height = BarUtils.getStatusBarHeight();
            mVBar.setLayoutParams(params);
        }
        setTitle(mTitle);
    }

    private void initEvent() {
        mTvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_back) {
            if (getContext() instanceof AppCompatActivity) {
                ((AppCompatActivity) getContext()).finish();
            }
        }
    }

    public void setBackTextString(String text){
        mTvBack.setText(text);
    }

    public void setTitle(String title) {
        mTitle = title;
        mTvTitle.setText(title);
    }

    public void setRightTextString(String text){
        mTvRight.setText(text);
    }

    public String getRightTextString(){
        return mTvRight.getText().toString();
    }

    public void setOnRightImageClickListener(OnClickListener listener) {
        mIvRight.setOnClickListener(listener);
    }

    public void setOnRightTextClickListener(OnClickListener listener) {
        mTvRight.setOnClickListener(listener);
    }

    public void setOnTextBackClickListener(OnClickListener listener){
        mTvBack.setOnClickListener(listener);
    }

    public void setRightTextVisible(int visible){
        mTvRight.setVisibility(visible);
    }

    public void setRightImageVisible(int visible){
        mIvRight.setVisibility(visible);
    }

    public void setRightTextClickable(boolean clickable){
        mTvRight.setClickable(clickable);
    }

    public void setRightTextAlpha(float alpha){
        mTvRight.setAlpha(alpha);
    }

    public void setRightTextColor(int color){
        mTvRight.setTextColor(color);
    }

    public void setRightTextEnable(boolean enable){
        mTvRight.setEnabled(enable);
    }

    public void setRightImageResource(int resId){
        mIvRight.setImageResource(resId);
        setRightImageVisible(View.VISIBLE);
    }

    public void setBackTextVisible(int visible){
        mTvBack.setVisibility(visible);
    }
}