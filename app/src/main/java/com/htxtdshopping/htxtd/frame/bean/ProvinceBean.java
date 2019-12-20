package com.htxtdshopping.htxtd.frame.bean;


import com.htxtdshopping.htxtd.frame.widget.wheelview.interfaces.IPickerViewData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenzhipeng
 */
public class ProvinceBean implements IPickerViewData {
    private String areaId;
    private String areaName;
    private List<CityBean> cities = new ArrayList<>();

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

    public List<CityBean> getCities() {
        return cities;
    }

    public void setCities(List<CityBean> cities) {
        this.cities = cities;
    }

    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return areaName;
    }
}
