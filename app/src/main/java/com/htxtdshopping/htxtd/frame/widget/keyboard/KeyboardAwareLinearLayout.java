/**
 * 判断软键盘是弹出还是隐藏
 */
package com.htxtdshopping.htxtd.frame.widget.keyboard;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.BarUtils;
import com.htxtdshopping.htxtd.frame.R;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import androidx.appcompat.widget.LinearLayoutCompat;


/**
 * LinearLayout that, when a view container, will report back when it thinks a soft keyboard
 * has been opened and what its height would be.
 */
public class KeyboardAwareLinearLayout extends LinearLayoutCompat {
    private static final String TAG = KeyboardAwareLinearLayout.class.getSimpleName();

    private final Rect rect = new Rect();
    private final Set<OnKeyboardHiddenListener> hiddenListeners = new HashSet<>();
    private final Set<OnKeyboardShownListener> shownListeners = new HashSet<>();
    private final int minKeyboardSize;
    private final int minCustomKeyboardSize;
    private final int defaultCustomKeyboardSize;
    private final int minCustomKeyboardTopMargin;

    private int viewInset;

    private boolean keyboardOpen = false;
    private int rotation = -1;

    public KeyboardAwareLinearLayout(Context context) {
        this(context, null);
    }

    public KeyboardAwareLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardAwareLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        minKeyboardSize = getResources().getDimensionPixelSize(R.dimen.min_keyboard_size);
        minCustomKeyboardSize = getResources().getDimensionPixelSize(R.dimen.min_custom_keyboard_size);
        defaultCustomKeyboardSize = getResources().getDimensionPixelSize(R.dimen.default_custom_keyboard_size);
        minCustomKeyboardTopMargin = getResources().getDimensionPixelSize(R.dimen.min_custom_keyboard_top_margin);
        viewInset = getViewInset();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        updateRotation();
        updateKeyboardState();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void updateRotation() {
        int oldRotation = rotation;
        rotation = getDeviceRotation();
        if (oldRotation != rotation) {
            Log.i(TAG, "rotation changed");
            onKeyboardClose();
        }
    }

    private void updateKeyboardState() {
        if (isLandscape()) {
            if (keyboardOpen) onKeyboardClose();
            return;
        }

        if (viewInset == 0 && Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP)
            viewInset = getViewInset();
        if (viewInset == 0){
            viewInset = BarUtils.getNavBarHeight();
        }
        final int availableHeight = this.getRootView().getHeight() - viewInset;

        getWindowVisibleDisplayFrame(rect);

        final int keyboardHeight = availableHeight - (rect.bottom - rect.top);

        if (keyboardHeight > minKeyboardSize) {
            if (!keyboardOpen) onKeyboardOpen(keyboardHeight);
        } else if (keyboardOpen) {
            onKeyboardClose();
        }
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    private int getViewInset() {
        try {
            Field attachInfoField = View.class.getDeclaredField("mAttachInfo");
            attachInfoField.setAccessible(true);
            Object attachInfo = attachInfoField.get(this);
            if (attachInfo != null) {
                Field stableInsetsField = attachInfo.getClass().getDeclaredField("mStableInsets");
                stableInsetsField.setAccessible(true);
                Rect insets = (Rect) stableInsetsField.get(attachInfo);
                return insets.bottom;
            }
        } catch (NoSuchFieldException nsfe) {
            Log.w(TAG, "field reflection error when measuring view inset", nsfe);
        } catch (IllegalAccessException iae) {
            Log.w(TAG, "access reflection error when measuring view inset", iae);
        }
        return 0;
    }

    protected void onKeyboardOpen(int keyboardHeight) {
        Log.i(TAG, "onKeyboardOpen(" + keyboardHeight + ")");
        keyboardOpen = true;

        notifyShownListeners();
    }

    protected void onKeyboardClose() {
        Log.i(TAG, "onKeyboardClose()");
        keyboardOpen = false;
        notifyHiddenListeners();
    }

    public boolean isKeyboardOpen() {
        return keyboardOpen;
    }

    public int getKeyboardHeight() {
        return isLandscape() ? getKeyboardLandscapeHeight() : getKeyboardPortraitHeight();
    }

    public boolean isLandscape() {
        int rotation = getDeviceRotation();
        return rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270;
    }

    private int getDeviceRotation() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Activity.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getRotation();
    }

    private int getKeyboardLandscapeHeight() {
        return Math.max(getHeight(), getRootView().getHeight()) / 2;
    }

    private int getKeyboardPortraitHeight() {
        return defaultCustomKeyboardSize;
    }

    public void postOnKeyboardClose(final Runnable runnable) {
        if (keyboardOpen) {
            addOnKeyboardHiddenListener(new OnKeyboardHiddenListener() {
                @Override
                public void onKeyboardHidden() {
                    removeOnKeyboardHiddenListener(this);
                    runnable.run();
                }
            });
        } else {
            runnable.run();
        }
    }

    public void postOnKeyboardOpen(final Runnable runnable) {
        if (!keyboardOpen) {
            addOnKeyboardShownListener(new OnKeyboardShownListener() {
                @Override
                public void onKeyboardShown() {
                    removeOnKeyboardShownListener(this);
                    runnable.run();
                }
            });
        } else {
            runnable.run();
        }
    }

    public void addOnKeyboardHiddenListener(OnKeyboardHiddenListener listener) {
        hiddenListeners.add(listener);
    }

    public void removeOnKeyboardHiddenListener(OnKeyboardHiddenListener listener) {
        hiddenListeners.remove(listener);
    }

    public void addOnKeyboardShownListener(OnKeyboardShownListener listener) {
        shownListeners.add(listener);
    }

    public void removeOnKeyboardShownListener(OnKeyboardShownListener listener) {
        shownListeners.remove(listener);
    }

    private void notifyHiddenListeners() {
        final Set<OnKeyboardHiddenListener> listeners = new HashSet<>(hiddenListeners);
        for (OnKeyboardHiddenListener listener : listeners) {
            listener.onKeyboardHidden();
        }
    }

    private void notifyShownListeners() {
        final Set<OnKeyboardShownListener> listeners = new HashSet<>(shownListeners);
        for (OnKeyboardShownListener listener : listeners) {
            listener.onKeyboardShown();
        }
    }

    public interface OnKeyboardHiddenListener {
        void onKeyboardHidden();
    }

    public interface OnKeyboardShownListener {
        void onKeyboardShown();
    }
}
