package com.android.dsly.common.widget;

/**
 * @author 陈志鹏
 * @date 2021/2/23
 */

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * 限制行数
 */
public class LimitedEditText extends AppCompatEditText {

    /**
     * Max lines to be present in editable text field
     */
    private int maxLines = 1;

    /**
     * application context;
     */
    private Context context;

    @Override
    public int getMaxLines() {
        return maxLines;
    }

    @Override
    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public LimitedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public LimitedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public LimitedEditText(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        this.addTextChangedListener(watcher);
    }

    private TextWatcher watcher = new TextWatcher() {
        String tmp;
        int cursor;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int line = getLineCount();
            if (line > maxLines) {
                if (before > 0 && start == 0) {
                    if (s.toString().equals(tmp)) {
                        // setText触发递归TextWatcher
                        cursor--;
                    } else {
                        // 手动移动光标为0
                        cursor = count - 1;
                    }
                } else {
                    cursor = start + count - 1;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // 限制可输入行数
            int line = getLineCount();
            if (line > maxLines) {
                String str = s.toString();
                tmp = str.substring(0, cursor) + str.substring(cursor + 1);
                setText(tmp);
                setSelection(cursor);
            }
        }
    };
}
