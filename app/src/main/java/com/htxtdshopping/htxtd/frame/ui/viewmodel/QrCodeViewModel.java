package com.htxtdshopping.htxtd.frame.ui.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;

import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.rxhttp.utils.TransformerUtils;
import com.blankj.utilcode.util.ConvertUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * @author 陈志鹏
 * @date 2021/3/10
 */
public class QrCodeViewModel extends BaseViewModel {

    public QrCodeViewModel(@NonNull Application application) {
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


    /**
     * 同步解析bitmap二维码
     */
    private MutableLiveData<String> mDecodeQRCodeLiveData = new MutableLiveData<>();

    public void decodeQRCode(Bitmap bitmap){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<String> emitter) throws Exception {
                String imgPath = QRCodeDecoder.syncDecodeQRCode(bitmap);
                emitter.onNext(imgPath);
                emitter.onComplete();
            }
        }).compose(TransformerUtils.pack(getLifecycleProvider()))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                        mDecodeQRCodeLiveData.setValue(result);
                    }
                });
    }

    public MutableLiveData<String> getDecodeQRCodeLiveData() {
        return mDecodeQRCodeLiveData;
    }
}