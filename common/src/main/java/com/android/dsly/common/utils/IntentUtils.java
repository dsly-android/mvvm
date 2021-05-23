package com.android.dsly.common.utils;

import android.content.Intent;
import android.os.Build;

import com.blankj.utilcode.util.UriUtils;

import java.io.File;

/**
 * @author 陈志鹏
 * @date 2021/4/22
 */
public class IntentUtils {

    /**
     * 获取文件的intent
     */
    public static Intent getPickFileIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        return intent;
    }

    /**
     * 用其他应用打开文件
     */
    public static Intent getOpenFileByOtherAppIntent(String filePath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        MediaFileUtils.MediaFileType mediaFileType = MediaFileUtils.getFileType(filePath);
        String fileType = null;
        if (mediaFileType != null) {
            fileType = mediaFileType.mimeType;
        } else {
            fileType = "*/*";
        }
        //设置intent的data和Type属性。
        intent.setDataAndType(UriUtils.file2Uri(new File(filePath)), fileType);
        return intent;
    }
}