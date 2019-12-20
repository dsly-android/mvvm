package com.android.dsly.common.base;

import android.app.Dialog;
import android.os.Bundle;

import com.blankj.utilcode.util.ReflectUtils;

import androidx.annotation.NonNull;

/**
 * @author 陈志鹏
 * @date 2018/10/26
 */
public class AppDialogFragment extends BaseDialogFragment {

    public static AppDialogFragment newInstance(Class cls) {
        AppDialogFragment fragment = new AppDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("class", cls);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Class cls = (Class) getArguments().getSerializable("class");
        return ReflectUtils.reflect(cls).newInstance(getContext()).get();
    }
}