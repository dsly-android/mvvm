package com.htxtdshopping.htxtd.frame.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.android.dsly.common.base.BaseActionItemPopup;
import com.android.dsly.common.utils.ToastUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.htxtdshopping.htxtd.frame.R;

/**
 * @author 陈志鹏
 * @date 2020/10/12
 */
public class TestActionItemPopup extends BaseActionItemPopup {

    public TestActionItemPopup(Context context) {
        super(context);
        addActionItem("aaa", R.drawable.ic_closed);
        addActionItem("bbb", R.drawable.ic_closed);
        addActionItem("ccc", R.drawable.ic_closed);
    }

    @Override
    public void onItemClick(int position) {
        switch (position){
            case 0:
                ToastUtils.showShort("aaa");
                break;
            case 1:
                ToastUtils.showShort("bbb");
                break;
            case 2:
                ToastUtils.showShort("ccc");
                break;
            default:
                break;
        }
    }

    @Override
    public void initPosition(View parent) {
        setGravity(Gravity.RIGHT);

        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        setMargin(0, location[1] + parent.getMeasuredHeight(), ConvertUtils.dp2px(12), 0);
    }
}
