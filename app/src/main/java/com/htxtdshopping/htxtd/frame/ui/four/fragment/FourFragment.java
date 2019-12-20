package com.htxtdshopping.htxtd.frame.ui.four.fragment;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseLazyFragment;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.ui.four.activity.ChooseChannelActivity;
import com.htxtdshopping.htxtd.frame.ui.four.activity.CommonTextViewActivity;
import com.htxtdshopping.htxtd.frame.ui.four.activity.CommonViewActivity;
import com.htxtdshopping.htxtd.frame.ui.four.activity.ContactNavigationActivity;
import com.htxtdshopping.htxtd.frame.ui.four.activity.DialogActivity;
import com.htxtdshopping.htxtd.frame.ui.four.activity.DoodleViewActivity;
import com.htxtdshopping.htxtd.frame.ui.four.activity.RadarViewActivity;
import com.htxtdshopping.htxtd.frame.ui.four.activity.TabHomeActivity;
import com.htxtdshopping.htxtd.frame.ui.four.activity.TimeOrAddressPickerActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenzhipeng
 */
public class FourFragment extends BaseLazyFragment {

    @BindView(R.id.v_bar)
    View mVBar;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_four;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(mVBar, getResources().getColor(R.color._81D8CF));
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_tab, R.id.btn_common_tv, R.id.btn_common, R.id.btn_time_or_date_picker,
            R.id.btn_contact_navigation, R.id.btn_choose_channel, R.id.btn_radarview, R.id.btn_dialog,
            R.id.btn_sketchpad})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tab:
                ActivityUtils.startActivity(TabHomeActivity.class);
                break;
            case R.id.btn_common_tv:
                ActivityUtils.startActivity(CommonTextViewActivity.class);
                break;
            case R.id.btn_common:
                ActivityUtils.startActivity(CommonViewActivity.class);
                break;
            case R.id.btn_time_or_date_picker:
                ActivityUtils.startActivity(TimeOrAddressPickerActivity.class);
                break;
            case R.id.btn_contact_navigation:
                ActivityUtils.startActivity(ContactNavigationActivity.class);
                break;
            case R.id.btn_choose_channel:
                ActivityUtils.startActivity(ChooseChannelActivity.class);
                break;
            case R.id.btn_radarview:
                ActivityUtils.startActivity(RadarViewActivity.class);
                break;
            case R.id.btn_dialog:
                ActivityUtils.startActivity(DialogActivity.class);
                break;
            case R.id.btn_sketchpad:
                ActivityUtils.startActivity(DoodleViewActivity.class);
                break;
            default:
                break;
        }
    }
}
