package com.htxtdshopping.htxtd.frame.utils;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.blankj.utilcode.util.ObjectUtils;

/**
 * @author 陈志鹏
 * @date 2021/2/8
 */
public class TextHighLightUtils {

    /**
     * 将content中包含filterStr的文字设置成其他颜色
     */
    public static SpannableStringBuilder getColoredString(String filterStr, String content) {
        if (ObjectUtils.isEmpty(filterStr)){
            return null;
        }
        SpannableStringBuilder messageText = new SpannableStringBuilder();
        String lowerCaseFilterStr = filterStr.toLowerCase();
        String lowerCaseText = content.toLowerCase();
        if (lowerCaseText.contains(lowerCaseFilterStr)) {
            SpannableStringBuilder finalBuilder = new SpannableStringBuilder();
            int length = content.length();
            int firstIndex = lowerCaseText.indexOf(lowerCaseFilterStr);
            String subString = content.substring(firstIndex);
            int restLength;
            if (subString != null) {
                restLength = subString.length();
            } else {
                restLength = 0;
            }
            if (length <= 12) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#0099ff")), firstIndex, firstIndex + filterStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return finalBuilder.append(spannableStringBuilder);
            } else {
                //首次出现搜索字符的index加上filter的length；
                int totalLength = firstIndex + filterStr.length();
                if (totalLength < 12) {
                    String smallerString = content.substring(0, 12);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(smallerString);
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#0099ff")), firstIndex, firstIndex + filterStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableStringBuilder.append("...");
                    return finalBuilder.append(spannableStringBuilder);
                } else if (restLength < 12) {
                    String smallerString = content.substring(length - 12, length);
                    String smallerStringLowerCase = lowerCaseText.substring(length - 12, length);
                    int index = smallerStringLowerCase.indexOf(lowerCaseFilterStr);
                    SpannableStringBuilder builder = new SpannableStringBuilder("...");
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(smallerString);
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#0099ff")), index, index + filterStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(spannableStringBuilder);
                    return finalBuilder.append(builder);
                } else {
                    String smallerString;
                    String smallerStringLowerCase;
                    int index = 0;
                    if (firstIndex >= 5) {
                        smallerString = content.substring(firstIndex - 5, firstIndex + 7);
                        smallerStringLowerCase = lowerCaseText.substring(firstIndex - 5, firstIndex + 7);
                        String smallerFilter = lowerCaseFilterStr;
                        if(smallerFilter.length() > 7){
                            smallerFilter = lowerCaseFilterStr.substring(0,7);
                        }
                        index = smallerStringLowerCase.indexOf(smallerFilter);
                    } else {
                        smallerString = content.substring(firstIndex, firstIndex + 12);
                        smallerStringLowerCase = lowerCaseText.substring(firstIndex, firstIndex + 12);
                        if (smallerStringLowerCase.length() < lowerCaseFilterStr.length()) {
                            index = 0;
                        }
                    }
                    SpannableStringBuilder builder = new SpannableStringBuilder("...");
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(smallerString);
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#0099ff")), index, getSmallerLength(smallerString.length(), index + filterStr.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(spannableStringBuilder);
                    builder.append("...");
                    return finalBuilder.append(builder);
                }
            }
        }
        return messageText;
    }

    private static int getSmallerLength(int stringLength, int endIndex) {
        return stringLength > endIndex + 1 ? endIndex : stringLength;
    }
}
