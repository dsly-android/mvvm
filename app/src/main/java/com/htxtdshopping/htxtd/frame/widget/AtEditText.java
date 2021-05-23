package com.htxtdshopping.htxtd.frame.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;

import com.htxtdshopping.htxtd.frame.R;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

public class AtEditText extends AppCompatEditText {

    public AtEditText(Context context) {
        super(context);
    }

    public AtEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AtEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 添加一个块,在文字的后面添加
     *
     * @param displayName 显示到界面的内容
     * @param userId      附加属性，比如用户id,邮件id之类的，如果不需要可以去除掉
     */
    public void addAtSpan(String displayName, long userId) {
        SpannableString spannableString = mentionSpannable( displayName, userId);
        int position = getSelectionEnd();
        position = position > 0 ? position - 1 : 0;
        getEditableText().replace(position, position + 1, spannableString);
    }

    private SpannableString mentionSpannable(String displayName, long userId) {
        String text = "@" + displayName + " ";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new MentionSpan(displayName, userId), 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        //向前删除一个字符，@后的内容必须大于一个字符，可以在后面加一个空格
        if (lengthBefore == 1 && lengthAfter == 0) {
            MentionSpan[] spans = getText().getSpans(0, getText().length(), MentionSpan.class);
            if (spans != null) {
                for (MentionSpan span : spans) {
                    if (getText().getSpanEnd(span) == start  && getText().getSpanFlags(span) == Spanned.SPAN_INCLUSIVE_EXCLUSIVE) {
                        getText().delete(getText().getSpanStart(span), getText().getSpanEnd(span));
                        getText().removeSpan(span);
                        break;
                    }
                }
            }
        }
    }

    /**
     * @的用户id
     */
    public Long getAtUserId(){
        MentionSpan[] mentions = getText().getSpans(0, getText().length(), MentionSpan.class);
        if (mentions != null && mentions.length > 0) {
            MentionSpan mention = mentions[0];
            return mention.getUserId();
        }
        return null;
    }

    private class MentionSpan extends ClickableSpan {
        private String displayName;
        private long userId;

        public MentionSpan(String displayName, long userId) {
            this.displayName = displayName;
            this.userId = userId;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getDisplayName() {
            return displayName;
        }

        public long getUserId() {
            return userId;
        }

        @Override
        public void onClick(@NonNull View widget) {

        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(getResources().getColor(android.R.color.black));
            ds.setUnderlineText(false);
        }
    }
}