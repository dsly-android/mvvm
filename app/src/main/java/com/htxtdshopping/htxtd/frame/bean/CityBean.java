package com.htxtdshopping.htxtd.frame.bean;


import com.contrarywind.interfaces.IPickerViewData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈志鹏
 * @date 2018/12/31
 */
public class CityBean implements IPickerViewData {
    private String areaId;
    private String areaName;
    private String provinceId;
    private List<CountyBean> counties = new ArrayList<>();

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

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public List<CountyBean> getCounties() {
        return counties;
    }

    public void setCounties(List<CountyBean> counties) {
        this.counties = counties;
    }

    @Override
    public String getPickerViewText() {
        return areaName;
    }
}
