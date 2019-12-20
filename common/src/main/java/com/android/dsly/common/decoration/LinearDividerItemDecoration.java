package com.android.dsly.common.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author 陈志鹏
 * @date 2018/11/8
 */
public class LinearDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private Drawable mDividerDrawable;
    private final Rect mBounds = new Rect();

    public LinearDividerItemDecoration(Context context, int widthOrHeight) {
        this(context, new ItemDecorationDrawable(context, widthOrHeight));
    }

    public LinearDividerItemDecoration(Context context, Drawable dividerDrawable) {
        mContext = context;
        mDividerDrawable = dividerDrawable;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            int orientation = ((LinearLayoutManager) manager).getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                this.drawVertical(c, parent);
            } else {
                this.drawHorizontal(c, parent);
            }
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int left;
        int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount - 1; ++i) {
            View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, this.mBounds);
            int bottom = this.mBounds.bottom + Math.round(child.getTranslationY());
            int top = bottom - mDividerDrawable.getIntrinsicHeight();
            mDividerDrawable.setBounds(left, top, right, bottom);
            mDividerDrawable.draw(canvas);
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int top;
        int bottom;
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top, parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount - 1; ++i) {
            View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, this.mBounds);
            int right = this.mBounds.right + Math.round(child.getTranslationX());
            int left = right - mDividerDrawable.getIntrinsicWidth();
            mDividerDrawable.setBounds(left, top, right, bottom);
            mDividerDrawable.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDividerDrawable == null) {
            outRect.set(0, 0, 0, 0);
        } else {
            RecyclerView.LayoutManager manager = parent.getLayoutManager();
            int position = manager.getPosition(view);
            boolean isLastRowOrColumn = isLastRowOrColumn(parent, position);
            if (!isLastRowOrColumn) {
                if (manager instanceof LinearLayoutManager) {
                    int orientation = ((LinearLayoutManager) manager).getOrientation();
                    if (orientation == LinearLayoutManager.VERTICAL) {
                        outRect.set(0, 0, 0, mDividerDrawable.getIntrinsicHeight());
                    } else {
                        outRect.set(0, 0, mDividerDrawable.getIntrinsicWidth(), 0);
                    }
                }
            }
        }
    }

    private boolean isLastRowOrColumn(RecyclerView parent, int position) {
        if (position == parent.getAdapter().getItemCount() - 1) {
            return true;
        } else {
            return false;
        }
    }
}