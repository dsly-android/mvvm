package com.htxtdshopping.htxtd.frame.ui.first.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;

import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.rxhttp.utils.TransformerUtils;
import com.blankj.utilcode.util.ConvertUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * @author 陈志鹏
 * @date 2021/3/10
 */
public class GenerateQrCodeViewModel extends BaseViewModel {

    public GenerateQrCodeViewModel(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<Bitmap> mGenerateQrCodeLiveData = new MutableLiveData<>();

    public void generateQrCode(String content) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> emitter) throws Exception {
                Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(content, ConvertUtils.dp2px(150));
                emitter.onNext(bitmap);
                emitter.onComplete();
            }
        }).compose(TransformerUtils.pack(getLifecycleProvider()))
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        mGenerateQrCodeLiveData.setValue(bitmap);
                    }
                });
    }

    public void generateBarCode(String content) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> emitter) throws Exception {
                int width = ConvertUtils.dp2px(150);
                int height = ConvertUtils.dp2px(70);
                Bitmap bitmap = QRCodeEncoder.syncEncodeBarcode(content, width, height, 0);
                emitter.onNext(bitmap);
                emitter.onComplete();
            }
        }).compose(TransformerUtils.pack(getLifecycleProvider()))
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        mGenerateQrCodeLiveData.setValue(bitmap);
                    }
                });
    }

    public MutableLiveData<Bitmap> getGenerateQrCodeLiveData() {
        return mGenerateQrCodeLiveData;
    }
}