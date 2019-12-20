package com.android.dsly.image_picker.local_data;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import com.android.dsly.image_picker.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

/**
 * @author 陈志鹏
 * @date 2018/12/10
 */
public class ImageDataUtils implements LoaderManager.LoaderCallbacks<Cursor> {

    //加载所有图片
    public static final int LOADER_ALL = 0;
    //分类加载图片
    public static final int LOADER_CATEGORY = 1;
    //查询图片需要的数据列
    private final String[] IMAGE_PROJECTION = {
            //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DISPLAY_NAME,
            //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.DATA,
            //图片的大小，long型  132492
            MediaStore.Images.Media.SIZE,
            //图片的宽度，int型  1920
            MediaStore.Images.Media.WIDTH,
            //图片的高度，int型  1080
            MediaStore.Images.Media.HEIGHT,
            //图片的类型     image/jpeg
            MediaStore.Images.Media.MIME_TYPE,
            //图片被添加的时间，long型  1450518608
            MediaStore.Images.Media.DATE_ADDED};

    private FragmentActivity mActivity;
    //图片加载完成的回调接口
    private OnImagesLoadedListener mLoadedListener;
    //所有的图片文件夹
    private ArrayList<ImageFolder> mImageFolders = new ArrayList<>();

    /**
     * @param activity       用于初始化LoaderManager，需要兼容到2.3
     * @param path           指定扫描的文件夹目录，可以为 null，表示扫描所有图片
     * @param loadedListener 图片加载完成的监听
     */
    public ImageDataUtils(FragmentActivity activity, String path, OnImagesLoadedListener loadedListener) {
        this.mActivity = activity;
        this.mLoadedListener = loadedListener;

        LoaderManager loaderManager = activity.getSupportLoaderManager();
        if (path == null) {
            //加载所有的图片
            loaderManager.initLoader(LOADER_ALL, null, this);
        } else {
            //加载指定目录的图片
            Bundle bundle = new Bundle();
            bundle.putString("path", path);
            loaderManager.initLoader(LOADER_CATEGORY, bundle, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        //扫描所有图片
        if (id == LOADER_ALL) {
            cursorLoader = new CursorLoader(mActivity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[6] + " DESC");
        }
        //扫描某个图片文件夹
        if (id == LOADER_CATEGORY) {
            cursorLoader = new CursorLoader(mActivity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[1] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[6] + " DESC");
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mImageFolders.clear();
        if (data != null) {
            //所有图片的集合,不分文件夹
            ArrayList<ImageItem> allImages = new ArrayList<>();
            while (data.moveToNext()) {
                //查询数据
                String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                String imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));

                File file = new File(imagePath);
                if (!file.exists() || file.length() <= 0) {
                    continue;
                }

                long imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                int imageWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                int imageHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                String imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                long imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
                //封装实体
                ImageItem imageItem = new ImageItem();
                imageItem.name = imageName;
                imageItem.path = imagePath;
                imageItem.size = imageSize;
                imageItem.width = imageWidth;
                imageItem.height = imageHeight;
                imageItem.mimeType = imageMimeType;
                imageItem.addTime = imageAddTime;
                allImages.add(imageItem);
                //根据父路径分类存放图片
                File imageFile = new File(imagePath);
                File imageParentFile = imageFile.getParentFile();
                ImageFolder imageFolder = new ImageFolder();
                imageFolder.name = imageParentFile.getName();
                imageFolder.path = imageParentFile.getAbsolutePath();

                if (!mImageFolders.contains(imageFolder)) {
                    ArrayList<ImageItem> images = new ArrayList<>();
                    images.add(imageItem);
                    imageFolder.cover = imageItem;
                    imageFolder.images = images;
                    mImageFolders.add(imageFolder);
                } else {
                    mImageFolders.get(mImageFolders.indexOf(imageFolder)).images.add(imageItem);
                }
            }
            //防止没有图片报异常
            if (data.getCount() > 0 && allImages.size() > 0) {
                //构造所有图片的集合
                ImageFolder allImagesFolder = new ImageFolder();
                allImagesFolder.name = mActivity.getResources().getString(R.string.image_all_pictures);
                allImagesFolder.path = "/";
                allImagesFolder.cover = allImages.get(0);
                allImagesFolder.images = allImages;
                //确保第一条是所有图片
                mImageFolders.add(0, allImagesFolder);
            }
        }
        mActivity.getSupportLoaderManager().destroyLoader(LOADER_CATEGORY);
        mActivity.getSupportLoaderManager().destroyLoader(LOADER_ALL);

        //回调接口，通知图片数据准备完成
        mLoadedListener.onImagesLoaded(mImageFolders);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    /**
     * 所有图片加载完成的回调接口
     */
    public interface OnImagesLoadedListener {
        /**
         * 图片加载完成
         * @param imageFolders
         */
        void onImagesLoaded(List<ImageFolder> imageFolders);
    }
}