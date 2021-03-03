package com.htxtdshopping.htxtd.frame.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import com.blankj.utilcode.util.UriUtils;

import java.util.HashMap;

/**
 * @author 陈志鹏
 * @date 2021/2/3
 */
public class MediaUtils {

    /**
     * 生成第一帧的图片
     *
     * @param url 图片路径
     * @param isLocal 是否是本地缓存的路径
     * @return
     */
    public static Bitmap getFirstFrameImage(Context context, String url, boolean isLocal) {
        Bitmap bitmap = null;
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        //的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (isLocal){
                //（）根据文件路径获取缩略图
                retriever.setDataSource(context, UriUtils.res2Uri(url));
            }else {
                //根据网络路径获取缩略图
                retriever.setDataSource(url, new HashMap());
            }
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    /**
     * 获取音频或视频文件的时间
     *
     * @param url 图片路径
     * @param isLocal 是否是本地缓存的路径
     */
    public static String getDuration(Context context, String url, boolean isLocal) {
        String duration = null;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            if (isLocal){
                //本地
                mmr.setDataSource(context, UriUtils.res2Uri(url));
            }else {
                //网络
                mmr.setDataSource(url, new HashMap());
            }
            duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mmr.release();
        }
        return duration;
    }
}