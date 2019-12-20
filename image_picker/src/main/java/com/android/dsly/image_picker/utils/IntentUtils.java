package com.android.dsly.image_picker.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;

import com.android.dsly.common.utils.ToastUtils;
import com.android.dsly.image_picker.activity.CropActivity;
import com.android.dsly.image_picker.activity.ImagePreviewActivity;
import com.android.dsly.image_picker.local_data.ImageItem;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.UriUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈志鹏
 * @date 2018/12/7
 */
public class IntentUtils {
    /**
     * 跳转到系统照相机
     */
    public static void toCapture(Activity activity, File tmpFile, int requestCode) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
            FileUtils.createOrExistsFile(tmpFile);
            if (FileUtils.isFileExists(tmpFile)) {
                // 默认情况下，即不需要指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // 照相机有自己默认的存储路径，拍摄的照片将返回一个缩略图。如果想访问原始图片，
                // 可以通过dataextra能够得到原始图片位置。即，如果指定了目标uri，data就没有数据，
                // 如果没有指定uri，则data就返回有数据！
                Uri uri = UriUtils.file2Uri(tmpFile);
                //加入uri权限 要不三星手机不能拍照
                List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    activity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                activity.startActivityForResult(cameraIntent, requestCode);
            } else {
                ToastUtils.showLong("图片错误");
            }
        } else {
            ToastUtils.showLong("没有系统相机");
        }
    }

    /**
     * 跳转图片预览页
     */
    public static void toImagePreview(Activity activity, ArrayList<ImageItem> allImages, String selectedPath, int maxSelectedNum, int requestCode) {
        Intent intent = new Intent(activity, ImagePreviewActivity.class);
        intent.putExtra(ImagePreviewActivity.KEY_All_IMAGE, allImages);
        intent.putExtra(ImagePreviewActivity.KEY_SELECTED_PATH, selectedPath);
        intent.putExtra(ImagePreviewActivity.KEY_MAX_SELECT_NUM, maxSelectedNum);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转图片裁剪页
     */
    public static void toImageCrop(Activity activity, String imagePath, int requestCode) {
        Intent intent = new Intent(activity, CropActivity.class);
        intent.putExtra(CropActivity.KEY_IMAGE_PATH, imagePath);
        activity.startActivityForResult(intent, requestCode);
    }
}