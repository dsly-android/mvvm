package com.htxtdshopping.htxtd.frame.widget.groupheadview.helper;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.android.dsly.common.utils.CacheDoubleUtils;
import com.android.dsly.rxhttp.utils.TransformerUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.htxtdshopping.htxtd.frame.widget.groupheadview.listener.OnHandlerListener;
import com.trello.rxlifecycle3.LifecycleProvider;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

public class CombineHelper {
    public static CombineHelper init() {
        return SingletonHolder.instance;
    }

    private CombineHelper() {

    }

    private static class SingletonHolder {
        private static final CombineHelper instance = new CombineHelper();
    }

    /**
     * 通过url加载
     *
     * @param builder
     */
    private void loadByUrls(final Builder builder) {
        int subSize = builder.subSize;
        Bitmap defaultBitmap = null;
        if (builder.placeholder != 0) {
            defaultBitmap = CompressHelper.getInstance()
                    .compressResource(builder.context.getResources(), builder.placeholder, subSize, subSize);
        }
        final ProgressHandler handler = new ProgressHandler(defaultBitmap, builder.count, new OnHandlerListener() {
            @Override
            public void onComplete(Bitmap[] bitmaps) {
                generateBitmap(builder, bitmaps);
            }
        });
        for (int i = 0; i < builder.count; i++) {
            final int finalI = i;
            if (builder.urls[i].contains("http://") || builder.urls[i].contains("https://") || builder.urls[i].contains("file://")) {
                Glide.with(builder.context)
                        .asBitmap()
                        .centerCrop()
                        .load(builder.urls[i])
                        .into(new SimpleTarget<Bitmap>(subSize, subSize) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                if (resource != null) {
                                    handler.obtainMessage(1, finalI, -1, resource).sendToTarget();
                                } else {
                                    handler.obtainMessage(2, finalI, -1, null).sendToTarget();
                                }
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                handler.obtainMessage(2, finalI, -1, null).sendToTarget();
                            }
                        });
            } else {
                handler.obtainMessage(2, finalI, -1, null).sendToTarget();
            }
        }
    }

    /**
     * 通过图片的资源id、bitmap加载
     *
     * @param builder
     */
    private void loadByResBitmaps(Builder builder) {
        int subSize = builder.subSize;
        Bitmap[] compressedBitmaps = new Bitmap[builder.count];
        for (int i = 0; i < builder.count; i++) {
            if (builder.resourceIds != null) {
                compressedBitmaps[i] = CompressHelper.getInstance()
                        .compressResource(builder.context.getResources(), builder.resourceIds[i], subSize, subSize);
            } else if (builder.bitmaps != null) {
                compressedBitmaps[i] = CompressHelper.getInstance()
                        .compressResource(builder.bitmaps[i], subSize, subSize);
            }
        }
        generateBitmap(builder, compressedBitmaps);
    }

    public void load(Builder builder) {
        if (builder.progressListener != null) {
            builder.progressListener.onStart();
        }
        obtainCacheBitmap(builder);
    }

    private void obtainCacheBitmap(Builder builder) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> emitter) throws Exception {
                Bitmap bitmap = CacheDoubleUtils.getInstance().getBitmap(Utils.hashKeyFormUrl(Arrays.toString(builder.urls)));
                if (bitmap != null) {
                    emitter.onNext(bitmap);
                    emitter.onComplete();
                } else {
                    emitter.onError(new NullPointerException("null"));
                }
            }
        }).compose(TransformerUtils.pack((LifecycleProvider) builder.context))
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        setBitmap(builder, bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (builder.urls != null) {
                            loadByUrls(builder);
                        } else {
                            loadByResBitmaps(builder);
                        }
                    }
                });
    }

    private void generateBitmap(final Builder b, Bitmap[] bitmaps) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> emitter) throws Exception {
                Bitmap result = b.layoutManager.combineBitmap(b.size, b.subSize, b.gap, b.gapColor, bitmaps);
                //保存生成的bitmap
                CacheDoubleUtils.getInstance().put(Utils.hashKeyFormUrl(Arrays.toString(b.urls)), result);
                emitter.onNext(result);
                emitter.onComplete();
            }
        }).compose(TransformerUtils.pack((LifecycleProvider) b.context))
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        setBitmap(b, bitmap);
                    }
                });
    }

    private void setBitmap(Builder b, Bitmap bitmap) {
        if (b == null) {
            return;
        }
        boolean isEquals = Utils.hashKeyFormUrl(Arrays.toString(b.urls)).equals(b.imageView.getTag());
        // 返回最终的组合Bitmap
        if (b.progressListener != null && isEquals) {
            b.progressListener.onComplete(b.imageView, bitmap);
        } else {
            // 给ImageView设置最终的组合Bitmap
            if (b.imageView != null && isEquals) {
                b.imageView.setImageBitmap(bitmap);
            }
        }
    }
}
