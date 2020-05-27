package com.htxtdshopping.htxtd.frame.ui.four.activity;

import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.decoration.sticky.StickyHeadContainer;
import com.android.dsly.common.decoration.sticky.StickyItemDecoration;
import com.android.dsly.common.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.bean.ContactBean;
import com.htxtdshopping.htxtd.frame.databinding.ActivityContactNavigationBinding;
import com.htxtdshopping.htxtd.frame.ui.four.adapter.ContactNavigationAdapter;
import com.htxtdshopping.htxtd.frame.widget.WaveSideBarView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ContactNavigationActivity extends BaseFitsWindowActivity<ActivityContactNavigationBinding, BaseViewModel> {

    private ContactNavigationAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_contact_navigation;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.rvContent.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvContent.addItemDecoration(new StickyItemDecoration(mBinding.shcSticky, ContactNavigationAdapter.HEADER));
        mAdapter = new ContactNavigationAdapter();
        mBinding.rvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (adapter.getItemViewType(position)) {
                    case ContactNavigationAdapter.CONTACT:
                        ToastUtils.showLong("contact");
                        break;
                    case ContactNavigationAdapter.HEADER:
                        ToastUtils.showLong("header");
                        break;
                    default:
                        break;
                }
            }
        });
        mBinding.wsbvLetter.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = mAdapter.getLetterPosition(letter);
                if (pos != -1) {
                    mBinding.rvContent.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager =
                            (LinearLayoutManager) mBinding.rvContent.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });
        mBinding.shcSticky.setDataCallback(new StickyHeadContainer.DataCallback() {
            @Override
            public void onDataChange(int pos) {
                ContactBean contactBean = mAdapter.getData().get(pos);
                mBinding.include.tvTip.setText(contactBean.getPys().substring(0, 1).toUpperCase());
            }
        });
    }

    @Override
    public void initData() {
        Observable.create(new ObservableOnSubscribe<List<ContactBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ContactBean>> e) throws Exception {
                List<ContactBean> contactBeans = JSON.parseArray(ContactBean.DATA, ContactBean.class);
                Collections.sort(contactBeans, new Comparator<ContactBean>() {
                    @Override
                    public int compare(ContactBean l, ContactBean r) {
                        if (l == null || r == null) {
                            return 0;
                        }

                        String lhsSortLetters = l.getPys().substring(0, 1).toUpperCase();
                        String rhsSortLetters = r.getPys().substring(0, 1).toUpperCase();
                        if (lhsSortLetters == null || rhsSortLetters == null) {
                            return 0;
                        }
                        return lhsSortLetters.compareTo(rhsSortLetters);
                    }
                });
                e.onNext(contactBeans);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(new Consumer<List<ContactBean>>() {
                    @Override
                    public void accept(List<ContactBean> contactBeans) throws Exception {
                        mAdapter.setNewData(contactBeans);
                    }
                });
    }
}
