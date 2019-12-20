package com.android.dsly.image_picker.local_data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 陈志鹏
 * @date 2018/12/11
 */
public class ImageFolder implements Serializable {

    private static final long serialVersionUID = -8830589488650195732L;

    /**当前文件夹的名字*/
    public String name;
    /**当前文件夹的路径*/
    public String path;
    /**当前文件夹需要要显示的缩略图，默认为最近的一次图片*/
    public ImageItem cover;
    /**当前文件夹下所有图片的集合*/
    public ArrayList<ImageItem> images;

    /** 只要文件夹的路径和名字相同，就认为是相同的文件夹 */
    @Override
    public boolean equals(Object o) {
        try {
            ImageFolder other = (ImageFolder) o;
            return this.path.equalsIgnoreCase(other.path) && this.name.equalsIgnoreCase(other.name);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}