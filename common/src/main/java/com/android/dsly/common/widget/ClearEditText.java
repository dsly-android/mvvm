package com.android.dsly.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.dsly.common.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author 陈志鹏
 * @date 2019-09-08
 */
public class ClearEditText extends FrameLayout {

    //文字默认颜色
    private int defaultColor = 0xFF373737;
    private int defaultHintColor = 0xFF999999;
    //下划线默认颜色
    private int defaultUnderLineColor = 0xfff0f0f0;

    private Context mContext;
    private EditText mEtInput;
    private ImageView mIvClose;
    private ImageView mIvLeft;
    private View mVUnderLine;
    private Drawable leftDrawable;
    private String hintTextStr;
    private String textStr;
    private int textColor;
    private int hintTextColor;
    private float textSize;
    private int mUnderLineColor;
    private Drawable closeDrawable;
    private int mUnderLineVisible;
    private int mPaddingLeftAndRight;

    public ClearEditText(@NonNull Context context) {
        this(context, null);
    }

    public ClearEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClearEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_clear_edittext, this);
        mContext = context;
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.ClearEditText);
        leftDrawable = array.getDrawable(R.styleable.ClearEditText_cet_drawableLeft);
        closeDrawable = array.getDrawable(R.styleable.ClearEditText_cet_drawableClose);
        hintTextStr = array.getString(R.styleable.ClearEditText_cet_hint);
        textStr = array.getString(R.styleable.ClearEditText_cet_text);
        textColor = array.getColor(R.styleable.ClearEditText_cet_textColor, defaultColor);
        hintTextColor = array.getColor(R.styleable.ClearEditText_cet_hintTextColor, defaultHintColor);
        textSize = array.getDimension(R.styleable.ClearEditText_cet_textSize, AutoSizeUtils.pt2px(mContext, 30));
        mUnderLineColor = array.getColor(R.styleable.ClearEditText_cet_underLineColor, defaultUnderLineColor);
        mUnderLineVisible = array.getInteger(R.styleable.ClearEditText_cet_underLineVisible, View.GONE);
        mPaddingLeftAndRight = array.getDimensionPixelSize(R.styleable.ClearEditText_cet_paddingLeftAndRight, AutoSizeUtils.pt2px(mContext, 20));

        initView();
        initEvent();
    }

    private void initView() {
        mEtInput = findViewById(R.id.et_input);
        mIvClose = findViewById(R.id.iv_close);
        mIvLeft = findViewById(R.id.iv_left);
        mVUnderLine = findViewById(R.id.v_underline);

        if (leftDrawable != null) {
            mIvLeft.setVisibility(VISIBLE);
            mIvLeft.setImageDrawable(leftDrawable);
            mIvLeft.setPadding(mPaddingLeftAndRight, 0, mPaddingLeftAndRight / 2, 0);
            mEtInput.setPadding(mPaddingLeftAndRight / 2, 0, mPaddingLeftAndRight / 2, 0);
        } else {
            mIvLeft.setVisibility(GONE);
            mEtInput.setPadding(mPaddingLeftAndRight, 0, mPaddingLeftAndRight / 2, 0);
        }
        mIvClose.setPadding(mPaddingLeftAndRight / 2, 0, mPaddingLeftAndRight, 0);
        if (closeDrawable != null) {
            mIvClose.setImageDrawable(closeDrawable);
        }
        if (!TextUtils.isEmpty(textStr) && textStr.length() > 0) {
            mIvClose.setVisibility(VISIBLE);
        } else {
            mIvClose.setVisibility(GONE);
        }
        mEtInput.setHint(hintTextStr);
        mEtInput.setText(textStr);
        if (!TextUtils.isEmpty(textStr)) {
            mEtInput.setSelection(textStr.length());
        }
        mEtInput.setTextColor(textColor);
        mEtInput.setHintTextColor(hintTextColor);
        mEtInput.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize);
        mVUnderLine.setBackgroundColor(mUnderLineColor);
        mVUnderLine.setVisibility(mUnderLineVisible);
    }

    private void initEvent() {
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mIvClose.setVisibility(VISIBLE);
                } else {
                    mIvClose.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mIvClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtInput.getText().clear();
            }
        });
    }

    /**
     * 获取输入的字符串
     * @return
     */
    public String getTextStr(){
        return mEtInput.getText().toString();
    }

    /**
     * 清空输入的内容
     */
    public void clear(){
        mEtInput.getText().clear();
    }
}