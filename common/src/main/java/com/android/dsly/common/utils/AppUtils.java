package com.android.dsly.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈志鹏
 * @date 2019-10-18
 */
public class AppUtils {

    /**
     * app是否存在
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppExist(Context context, String packageName){
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> pName = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(pinfo != null){
            for(int i = 0; i < pinfo.size(); i++){
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        //判断pName中是否有目标程序的包名，有TRUE，没有FALSE
        return pName.contains(packageName);
    }
}
