package com.htxtdshopping.htxtd.frame.ui.four.activity;

import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.android.dsly.common.base.BaseActivity;
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

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ContactNavigationActivity extends BaseActivity<ActivityContactNavigationBinding, BaseViewModel> {

    private ContactNavigationAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_contact_navigation;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.rvContent.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvContent.addItemDecoration(new StickyItemDecoration(mBinding.shcSticky, WaveSideBarView.ITEM_LETTER));
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
                    case WaveSideBarView.ITEM_LETTER:
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
                int pos = mBinding.wsbvLetter.getLetterPosition(mAdapter.getData(), letter);
                if (pos != -1) {
                    scrollToPos(pos);
                }
            }
        });
        mBinding.shcSticky.setDataCallback(new StickyHeadContainer.DataCallback() {
            @Override
            public void onDataChange(int pos) {
                WaveSideBarView.IDisplayName displayName = mAdapter.getData().get(pos);
                mBinding.include.tvTip.setText(displayName.getDisplayName());
            }
        });
    }

    @Override
    public void initData() {
        Observable.create(new ObservableOnSubscribe<List<WaveSideBarView.IDisplayName>>() {
            @Override
            public void subscribe(ObservableEmitter<List<WaveSideBarView.IDisplayName>> e) throws Exception {
                List<ContactBean> contactBeans = JSON.parseArray(ContactBean.DATA, ContactBean.class);
                List<WaveSideBarView.IDisplayName> iDisplayNames = mBinding.wsbvLetter.sortData(contactBeans);
                e.onNext(iDisplayNames);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(new Consumer<List<WaveSideBarView.IDisplayName>>() {
                    @Override
                    public void accept(List<WaveSideBarView.IDisplayName> contactBeans) throws Exception {
                        mAdapter.setNewData(contactBeans);
                    }
                });
    }

    private void scrollToPos(int pos){
        LinearLayoutManager mLayoutManager =
                (LinearLayoutManager) mBinding.rvContent.getLayoutManager();
        mLayoutManager.scrollToPositionWithOffset(pos, 0);
    }
}
