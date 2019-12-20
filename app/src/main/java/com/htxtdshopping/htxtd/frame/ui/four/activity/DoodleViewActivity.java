package com.htxtdshopping.htxtd.frame.ui.four.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.widget.TitleBar;
import com.blankj.utilcode.util.ConvertUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.widget.doodleView.DoodleView;

import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;

public class DoodleViewActivity extends BaseFitsWindowActivity {

    @BindView(R.id.tb_title)
    TitleBar mTbTitle;
    @BindView(R.id.iv_right)
    ImageView mIvRight;
    @BindView(R.id.dv_view)
    DoodleView dvView;

    private AlertDialog mColorDialog;
    private AlertDialog mPaintDialog;
    private AlertDialog mShapeDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_doodle_view;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTbTitle.setRightImageVisible(View.VISIBLE);
        mTbTitle.setRightImageResource(R.drawable.ic_launcher_foreground);
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
        return dvView.onTouchEvent(event);
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
                dvView.reset();
                break;
            case R.id.main_save:
                String path = dvView.saveBitmap(dvView);
                Toast.makeText(this, "保存图片的路径为：" + path, Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_back:
                dvView.back();
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
                                            dvView.setColor("#0000ff");
                                            break;
                                        case 1:
                                            dvView.setColor("#ff0000");
                                            break;
                                        case 2:
                                            dvView.setColor("#272822");
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
                                            dvView.setSize(ConvertUtils.dp2px(5));
                                            break;
                                        case 1:
                                            dvView.setSize(ConvertUtils.dp2px(10));
                                            break;
                                        case 2:
                                            dvView.setSize(ConvertUtils.dp2px(15));
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
                                            dvView.setType(DoodleView.ActionType.Path);
                                            break;
                                        case 1:
                                            dvView.setType(DoodleView.ActionType.Line);
                                            break;
                                        case 2:
                                            dvView.setType(DoodleView.ActionType.Rect);
                                            break;
                                        case 3:
                                            dvView.setType(DoodleView.ActionType.Circle);
                                            break;
                                        case 4:
                                            dvView.setType(DoodleView.ActionType.FillEcRect);
                                            break;
                                        case 5:
                                            dvView.setType(DoodleView.ActionType.FilledCircle);
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
