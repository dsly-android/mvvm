package com.htxtdshopping.htxtd.frame.widget.easyfloat;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.WindowManager;

import com.lzf.easyfloat.enums.SidePattern;
import com.lzf.easyfloat.interfaces.OnAppFloatAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * AppFloat透明动画
 * @author 陈志鹏
 * @date 2019-10-14
 */
public class AppFloatAlphaAnimator implements OnAppFloatAnimator {

    @Nullable
    @Override
    public Animator enterAnim(@NonNull View view, @NonNull WindowManager.LayoutParams params, @NonNull WindowManager windowManager, @NonNull SidePattern sidePattern) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.alpha = (float) animation.getAnimatedValue();
                windowManager.updateViewLayout(view, params);
            }
        });
        return animator;
    }

    @Nullable
    @Override
    public Animator exitAnim(@NonNull View view, @NonNull WindowManager.LayoutParams params, @NonNull WindowManager windowManager, @NonNull SidePattern sidePattern) {
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.alpha = (float) animation.getAnimatedValue();
                windowManager.updateViewLayout(view, params);
            }
        });
        return animator;
    }
}
