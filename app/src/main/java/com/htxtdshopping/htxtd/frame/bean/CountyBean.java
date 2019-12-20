package com.htxtdshopping.htxtd.frame.bean;

import com.htxtdshopping.htxtd.frame.widget.wheelview.interfaces.IPickerViewData;

/**
 * @author 陈志鹏
 * @date 2018/12/31
 */
public class CountyBean implements IPickerViewData {
    private String areaId;
    private String areaName;
    private String cityId;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    @Override
    public String getPickerViewText() {
        return areaName;
    }
}
