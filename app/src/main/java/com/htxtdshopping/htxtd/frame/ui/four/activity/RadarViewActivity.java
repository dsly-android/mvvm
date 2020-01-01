package com.htxtdshopping.htxtd.frame.ui.four.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.ConvertUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityRadarViewBinding;
import com.htxtdshopping.htxtd.frame.widget.radarview.RadarData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RadarViewActivity extends BaseFitsWindowActivity<ActivityRadarViewBinding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_radar_view;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        mBinding.rvRadar.setRotationEnable(false);

        mBinding.rvRadar.setEmptyHint("无数据");

        List<Integer> layerColor = new ArrayList<>();
        Collections.addAll(layerColor, 0x3300bcd4, 0x3303a9f4, 0x335677fc, 0x333f51b5, 0x33673ab7);
        mBinding.rvRadar.setLayerColor(layerColor);

        List<String> vertexText = new ArrayList<>();
        Collections.addAll(vertexText, "力量", "敏捷", "速度", "智力", "精神", "耐力", "体力", "魔力", "意志", "幸运");
        mBinding.rvRadar.setVertexText(vertexText);

        /*List<Integer> res = new ArrayList<>();
        Collections.addAll(res, R.drawable.umeng_socialize_wxcircle, R.drawable.umeng_socialize_wxcircle, R.drawable.umeng_socialize_wxcircle,
                R.drawable.umeng_socialize_wxcircle, R.drawable.umeng_socialize_wxcircle, R.drawable.umeng_socialize_wxcircle,
                R.drawable.umeng_socialize_wxcircle, R.drawable.umeng_socialize_wxcircle, R.drawable.umeng_socialize_wxcircle, R.drawable.umeng_socialize_wxcircle);
        mRvRadar.setVertexIconResid(res);*/

        List<Float> values = new ArrayList<>();
        Collections.addAll(values, 3f, 6f, 2f, 7f, 5f, 1f, 9f, 3f, 8f, 5f);
        RadarData data = new RadarData(values);
        mBinding.rvRadar.addData(data);

        List<Float> values2 = new ArrayList<>();
        Collections.addAll(values2, 7f, 1f, 4f, 2f, 8f, 3f, 4f, 6f, 5f, 3f);
        RadarData data2 = new RadarData(values2);
        data2.setValueTextEnable(true);
        data2.setColor(Color.BLUE);
        data2.setVauleTextColor(Color.WHITE);
        data2.setValueTextSize(ConvertUtils.dp2px(10));
        data2.setLineWidth(ConvertUtils.dp2px(1));
        mBinding.rvRadar.addData(data2);
    }
}
