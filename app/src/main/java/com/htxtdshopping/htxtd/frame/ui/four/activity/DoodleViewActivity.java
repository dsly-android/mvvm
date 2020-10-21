package com.htxtdshopping.htxtd.frame.ui.four.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.ConvertUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityDoodleViewBinding;
import com.htxtdshopping.htxtd.frame.widget.doodleView.DoodleView;

import androidx.appcompat.app.AlertDialog;

public class DoodleViewActivity extends BaseActivity<ActivityDoodleViewBinding, BaseViewModel> {

    private AlertDialog mColorDialog;
    private AlertDialog mPaintDialog;
    private AlertDialog mShapeDialog;

    private ImageView mIvRight;

    @Override
    public int getLayoutId() {
        return R.layout.activity_doodle_view;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.tbTitle.setRightImageVisible(View.VISIBLE);
        mBinding.tbTitle.setRightImageResource(R.drawable.ic_launcher_foreground);

        mIvRight = findViewById(R.id.iv_right);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        registerForContextMenu(mIvRight);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mBinding.dvView.onTouchEvent(event);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.doodle_menu_main, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_color:
                showColorDialog();
                break;
            case R.id.main_size:
                showSizeDialog();
                break;
            case R.id.main_action:
                showShapeDialog();
                break;
            case R.id.main_reset:
                mBinding.dvView.reset();
                break;
            case R.id.main_save:
                String path = mBinding.dvView.saveBitmap(mBinding.dvView);
                Toast.makeText(this, "保存图片的路径为：" + path, Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_back:
                mBinding.dvView.back();
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 显示选择画笔颜色的对话框
     */
    private void showColorDialog() {
        if (mColorDialog == null) {
            mColorDialog = new AlertDialog.Builder(this)
                    .setTitle("选择颜色")
                    .setSingleChoiceItems(new String[]{"蓝色", "红色", "黑色"}, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            mBinding.dvView.setColor("#0000ff");
                                            break;
                                        case 1:
                                            mBinding.dvView.setColor("#ff0000");
                                            break;
                                        case 2:
                                            mBinding.dvView.setColor("#272822");
                                            break;
                                        default:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        }
        mColorDialog.show();
    }

    /**
     * 显示选择画笔粗细的对话框
     */
    private void showSizeDialog() {
        if (mPaintDialog == null) {
            mPaintDialog = new AlertDialog.Builder(this)
                    .setTitle("选择画笔粗细")
                    .setSingleChoiceItems(new String[]{"细", "中", "粗"}, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            mBinding.dvView.setSize(ConvertUtils.dp2px(5));
                                            break;
                                        case 1:
                                            mBinding.dvView.setSize(ConvertUtils.dp2px(10));
                                            break;
                                        case 2:
                                            mBinding.dvView.setSize(ConvertUtils.dp2px(15));
                                            break;
                                        default:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        }
        mPaintDialog.show();
    }

    /**
     * 显示选择画笔形状的对话框
     */
    private void showShapeDialog() {
        if (mShapeDialog == null) {
            mShapeDialog = new AlertDialog.Builder(this)
                    .setTitle("选择形状")
                    .setSingleChoiceItems(new String[]{"路径", "直线", "矩形", "圆形", "实心矩形", "实心圆"}, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            mBinding.dvView.setType(DoodleView.ActionType.Path);
                                            break;
                                        case 1:
                                            mBinding.dvView.setType(DoodleView.ActionType.Line);
                                            break;
                                        case 2:
                                            mBinding.dvView.setType(DoodleView.ActionType.Rect);
                                            break;
                                        case 3:
                                            mBinding.dvView.setType(DoodleView.ActionType.Circle);
                                            break;
                                        case 4:
                                            mBinding.dvView.setType(DoodleView.ActionType.FillEcRect);
                                            break;
                                        case 5:
                                            mBinding.dvView.setType(DoodleView.ActionType.FilledCircle);
                                            break;
                                        default:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        }
        mShapeDialog.show();
    }
}
