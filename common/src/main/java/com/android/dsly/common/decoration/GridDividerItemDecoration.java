package com.android.dsly.common.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author 陈志鹏
 * @date 2018/11/5
 */
public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private int mDividerWidth;
    private int mDividerHeight;

    public GridDividerItemDecoration(Context context, int dividerWidthAndHeight) {
        this(context, dividerWidthAndHeight, dividerWidthAndHeight);
    }

    public GridDividerItemDecoration(Context context, int dividerWidth, int dividerHeight) {
        this(context, dividerWidth, dividerHeight, android.R.color.transparent);
    }

    public GridDividerItemDecoration(Context context, int dividerWidth, int dividerHeight, int color) {
        mDividerWidth = AutoSizeUtils.pt2px(context, dividerWidth);
        mDividerHeight = AutoSizeUtils.pt2px(context, dividerHeight);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(color));
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int itemPosition = parent.getLayoutManager().getPosition(view);
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int orientation = ((GridLayoutManager) layoutManager).getOrientation();
            if (orientation == GridLayoutManager.VERTICAL) {
                boolean isLastRow = isLastRow(parent, itemPosition, spanCount, childCount);

                int eachWidth = (spanCount - 1) * mDividerWidth / spanCount;
                int dl = mDividerWidth - eachWidth;

                int left = itemPosition % spanCount * dl;
                int right = eachWidth - left;
                int bottom = mDividerHeight;
                if (isLastRow) {
                    bottom = 0;
                }
                outRect.set(left, 0, right, bottom);
            } else {
                boolean isLastColumn = isLastColumn(parent, itemPosition, spanCount, childCount);

                int eachHeight = (spanCount - 1) * mDividerHeight / spanCount;
                int dl = mDividerHeight - eachHeight;

                int top = itemPosition % spanCount * dl;
                int bottom = eachHeight - top;
                int right = mDividerWidth;
                if (isLastColumn) {
                    right = 0;
                }
                outRect.set(0, top, right, bottom);
            }
        } else {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                boolean isLastRow = isLastRow(parent, itemPosition, spanCount, childCount);

                int eachWidth = (spanCount - 1) * mDividerWidth / spanCount;
                int dl = mDividerWidth - eachWidth;

                int left = itemPosition % spanCount * dl;
                int right = eachWidth - left;
                int bottom = mDividerHeight;
                if (isLastRow) {
                    bottom = 0;
                }
                outRect.set(left, 0, right, bottom);
            } else {
                boolean isLastColumn = isLastColumn(parent, itemPosition, spanCount, childCount);

                int eachHeight = (spanCount - 1) * mDividerHeight / spanCount;
                int dl = mDividerHeight - eachHeight;

                int top = itemPosition % spanCount * dl;
                int bottom = eachHeight - top;
                int right = mDividerWidth;
                if (isLastColumn) {
                    right = 0;
                }
                outRect.set(0, top, right, bottom);
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        draw(c, parent);
    }

    /**
     * 绘制item分割线
     */
    private void draw(Canvas canvas, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            boolean isLastRow = isLastRow(parent, i, getSpanCount(parent), childCount);
            boolean isLastColumn = isLastColumn(parent, i, getSpanCount(parent), childCount);
            //画水平分隔线
            int left = child.getLeft();
            int right = child.getRight();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDividerHeight;
            if (isLastRow){
                bottom = top;
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
            //画垂直分割线
            top = child.getTop();
            bottom = child.getBottom() + mDividerHeight;
            left = child.getRight() + params.rightMargin;
            right = left + mDividerWidth;
            if (isLastColumn){
                right = left;
            }
            if (isLastRow){
                bottom = child.getBottom();
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    private boolean isLastColumn(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int orientation = ((GridLayoutManager) layoutManager).getOrientation();
            if (orientation == GridLayoutManager.VERTICAL) {
                // 如果是最后一列，则不需要绘制右边
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            } else {
                int lines = childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
                return lines == pos / spanCount + 1;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是最后一列，则不需要绘制右边
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            } else {
                int lines = childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
                return lines == pos / spanCount + 1;
            }
        }
        return false;
    }

    private boolean isLastRow(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int orientation = ((GridLayoutManager) layoutManager).getOrientation();
            if (orientation == GridLayoutManager.VERTICAL) {
                int lines = childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
                return lines == pos / spanCount + 1;
            } else {
                if (pos % spanCount == spanCount - 1) {
                    return true;
                } else {
                    return false;
                }
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                int lines = childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
                return lines == pos / spanCount + 1;
            } else {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isFirstRow(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int orientation = ((GridLayoutManager) layoutManager).getOrientation();
            if (orientation == GridLayoutManager.VERTICAL) {
                if (pos / spanCount == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (pos % spanCount == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if (pos / spanCount == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (pos % spanCount == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 获取列数
     */
    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }
}
