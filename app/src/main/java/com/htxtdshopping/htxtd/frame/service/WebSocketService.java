package com.htxtdshopping.htxtd.frame.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.android.dsly.common.base.BaseService;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.htxtdshopping.htxtd.frame.event.SocketReceiveEvent;
import com.htxtdshopping.htxtd.frame.event.SocketSendEvent;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.lang.ref.WeakReference;
import java.net.URI;

import androidx.annotation.Nullable;

/**
 * @author 陈志鹏
 * @date 2019-11-29
 */
public class WebSocketService extends BaseService {

    //每隔10秒进行一次对长连接的心跳检测
    private static final long HEART_BEAT_RATE = 10 * 1000;
    //心跳what
    private static final int WHAT_HEART_BEAT = 1;

    private WebSocketClient mClient;
    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new WebSocketHandler(this);
    }

    public static class WebSocketHandler extends Handler {

        private WeakReference<WebSocketService> mService;

        public WebSocketHandler(WebSocketService service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WebSocketClient client = mService.get().getClient();
            if (client.isClosed()) {
                mService.get().reconnectWs();
            }
            //每隔一定的时间，对长连接进行一次心跳检测
            sendEmptyMessageDelayed(WHAT_HEART_BEAT, HEART_BEAT_RATE);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ThreadUtils.executeByIo(new ThreadUtils.SimpleTask<Object>() {
            @Override
            public Object doInBackground() throws Throwable {
                if (mClient == null) {
                    initSocketClient();
                    //开启心跳检测
                    mHandler.sendEmptyMessageDelayed(WHAT_HEART_BEAT, HEART_BEAT_RATE);
                }
                return null;
            }

            @Override
            public void onSuccess(Object result) {

            }
        });
        return Service.START_REDELIVER_INTENT;
    }

    /**
     * 初始化websocket连接
     */
    private void initSocketClient() {
        URI uri = URI.create("ws://192.168.0.168:8088/ws");
        mClient = new WebSocketClient(uri) {

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                LogUtils.e("onOpen");
            }

            @Override
            public void onMessage(String message) {
                SocketReceiveEvent event = new SocketReceiveEvent();
                event.setMsg(message);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                LogUtils.e("onClose");
            }

            @Override
            public void onError(Exception ex) {
                LogUtils.e("onError：" + ex.getMessage());
            }
        };
        try {
            mClient.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     */
    @Subscriber
    public void sendMsg(SocketSendEvent event) {
        if (null != mClient) {
            mClient.send(event.getMsg());
        }
    }

    /**
     * 开启重连
     */
    private void reconnectWs() {
        mHandler.removeMessages(WHAT_HEART_BEAT);
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Object>() {
            @Override
            public Object doInBackground() throws Throwable {
                try {
                    mClient.reconnectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //开启心跳检测
                mHandler.sendEmptyMessageDelayed(WHAT_HEART_BEAT, HEART_BEAT_RATE);
                return null;
            }

            @Override
            public void onSuccess(Object result) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(WHAT_HEART_BEAT);
        closeConnect();
    }

    /**
     * 断开连接
     */
    private void closeConnect() {
        try {
            if (null != mClient) {
                mClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mClient = null;
        }
    }

    public WebSocketClient getClient() {
        return mClient;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
