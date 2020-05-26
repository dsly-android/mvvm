package com.htxtdshopping.htxtd.frame.ui.first.viewmodel;

import android.app.Application;

import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.rxhttp.observer.CommonObserver;
import com.android.dsly.rxhttp.utils.RxLifecycleUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.htxtdshopping.htxtd.frame.bean.NewsPictureBean;
import com.htxtdshopping.htxtd.frame.bean.NewsTextBean;
import com.htxtdshopping.htxtd.frame.bean.NewsVideoBean;
import com.htxtdshopping.htxtd.frame.bean.RefreshBean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 陈志鹏
 * @date 2019-12-27
 */
public class RefreshAndLoadMoreViewModel extends BaseViewModel {

    private MutableLiveData<RefreshBean<List<MultiItemEntity>>> liveData = new MutableLiveData<>();

    public RefreshAndLoadMoreViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadData(boolean isRefresh) {
        Observable.create(new ObservableOnSubscribe<List<MultiItemEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<MultiItemEntity>> e) throws Exception {
                List<MultiItemEntity> datas = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    switch (i % 3) {
                        case 0:
                            NewsTextBean newsTextBean = new NewsTextBean();
                            newsTextBean.setTitle("《红色通缉》第五集《筑坝》：红通逃犯欲潜逃国外 最终却在国内被捕");
                            datas.add(newsTextBean);
                            break;
                        case 1:
                            NewsPictureBean newsPictureBean = new NewsPictureBean();
                            newsPictureBean.setTitle("早报：发布手机12.1首个测试版—找回群聊功能，强大！");
                            datas.add(newsPictureBean);
                            break;
                        case 2:
                            NewsVideoBean newsVideoBean = new NewsVideoBean();
                            newsVideoBean.setTitle("“圣诞老人”在山上抛洒积雪");
                            newsVideoBean.setVideoUrl("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
                            datas.add(newsVideoBean);
                            break;
                        default:
                            break;
                    }
                }
                try {
                    Thread.sleep(2000);
                } catch (Exception e1) {

                }
                e.onNext(datas);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindUntilDestroyEvent(getLifecycleProvider()))
                .subscribe(new CommonObserver<List<MultiItemEntity>>() {
                    @Override
                    protected void onSuccess(List<MultiItemEntity> multiItemEntities) {
                        RefreshBean<List<MultiItemEntity>> refreshResponse = new RefreshBean<>();
                        refreshResponse.setRefresh(isRefresh);
                        refreshResponse.setData(multiItemEntities);
                        liveData.setValue(refreshResponse);
                        showDialog(false);
                    }

                    @Override
                    protected void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        liveData.setValue(null);
                        showDialog(false);
                    }
                });
    }

    public MutableLiveData<RefreshBean<List<MultiItemEntity>>> getLiveData() {
        return liveData;
    }
}
