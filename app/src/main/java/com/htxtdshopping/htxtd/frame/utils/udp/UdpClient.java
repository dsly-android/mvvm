package com.htxtdshopping.htxtd.frame.utils.udp;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * udp端口服务监听类
 * 指定某端口监听
 */
public abstract class UdpClient {

    private int port;
    private int max = 300 * 1024;//字节数 单位 字节
    private DatagramSocket datagramSocket;
    private String roomId;
    private String userId;

    private String serverIp;
    private int serverPort;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        if (this.status == 2) {
            return;
        }
        this.status = status;
    }

    /**
     * 状态：
     * 0：初始化
     * 1: 等待接听
     * 2：开始通话
     */
    private int status = 0;

    private Disposable receiveMsgDis;

    private void runClient() {
        receiveMsgDis = Observable.interval(0, 0, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        try {
                            if (datagramSocket == null) {
                                return;
                            }
                            byte[] buffer = new byte[max];
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                            LogUtils.i("udp启动开始监听:" + port);
                            datagramSocket.receive(packet);
                            byte[] buf = packet.getData();
                            String reqStr = new String(buf, "UTF-8");
                            LogUtils.i("客户端收到消息:" + reqStr);
                            //空数据不处理
                            if (!TextUtils.isEmpty(reqStr.trim())) {
                                excute(reqStr);
                            }
                        } catch (Exception e) {
                            //报错不处理，依旧会继续监听
                            e.printStackTrace();
                        }
                    }
                });
    }

    //报文处理
    public void excute(String str) {
        if (!TextUtils.isEmpty(str)) {
            String[] arr = str.split("\\|");
            if (arr.length >= 1) {
                switch (arr[0].trim()) {
                    //上报地址回调状态1 等待对方接听
                    case Commons.RESPONSE_WAIT:
                        LogUtils.i("收到响应，尝试连接成功--等待接听");
                        setStatus(1);
                        callWait();
                        break;
                    //上报地址回调状态1 等待对方接听
                    case Commons.START:
                        LogUtils.i("收到响应，尝试连接成功--开始通话");
                        setStatus(2);
                        break;
                    case Commons.PONG:
                        LogUtils.i("收到服务端心跳，暂不处理业务");
                        break;
                    case Commons.RESPONSE_STREAM:
//                        完整报文格式
//                        RESPONSE_STREAM|START|数据流字符串|END
                        if (arr.length != 4) {
                            throw new RuntimeException("数据流报文格式错误");
                        }
                        if (!"START".equals(arr[1].trim())) {
                            throw new RuntimeException("数据流报文格式错误");
                        }
                        if (!"END".equals(arr[3].trim())) {
                            throw new RuntimeException("数据流报文格式错误--接收的数据数组长度不够");
                        }
                        listen(arr[2]);
                        break;
                }

            }
        }
    }

    /**
     * 启动监听
     * 自行代入
     * 房间id
     * 用户id
     * 由http请求获取
     */
    public void start(String serverIp, int serverPort, int port, String roomId, String userId) {
        this.port = port;
        this.userId = userId;
        this.roomId = roomId;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        if (this.port == 0) {
            this.port = new Random().nextInt(10000) + 1000;
            LogUtils.i("端口未定义，随机生成开放udp端口:" + port);
        }
        if (TextUtils.isEmpty(serverIp) || TextUtils.isEmpty(roomId) || TextUtils.isEmpty(userId)) {
            throw new RuntimeException("用户id或者房间id为空");
        }
        if (serverPort == 0) {
            throw new RuntimeException("服务端端口错误");
        }
        //监听线程
        runClient();

        try {
            //解决端口被占用问题
            datagramSocket = new DatagramSocket(null);
            datagramSocket.setReuseAddress(true);
            datagramSocket.bind(new InetSocketAddress(port));
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        this.connect();
    }

    private Disposable mConnectDisposable;

    /**
     * 连接线程
     */
    private void connect() {
        mConnectDisposable = Observable.interval(0, 500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //等待或者初始化 进行不断上报地址 尝试连接
                        LogUtils.e("connect:" + status);
                        if (status == 0 || status == 1) {
                            try {
                                if (status == 1) {
                                    //持续通知等待接听状态
                                    callWait();
                                }
                                //一秒钟上报2次
                                submitAddress();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //开始通话
                        } else if (status == 2) {
                            startCall();
                            heartThread();
                            mConnectDisposable.dispose();
                        }
                    }
                });
    }

    private Disposable mHeartDisposable;

    /**
     * 开始通话后，启动心跳线程
     */
    private void heartThread() {
        mHeartDisposable = Observable.interval(0, 5000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        pushToClient(serverIp, serverPort, PackMsgUtil.ping(roomId, userId));
                    }
                });
    }

    /**
     * 上报地址
     */
    protected void submitAddress() throws Exception {
        if (status == 0) {
            LogUtils.i("开始上报地址，测试连通性");
            if (this.datagramSocket != null) {
                pushToClient(this.serverIp, this.serverPort, PackMsgUtil.submitAddress(this.roomId, this.userId));
            } else {
                throw new RuntimeException("socket对象初始化失败!");
            }
        }
    }

    /**
     * 开始通话，就可以调用上报数据流方法
     *
     * @param stream
     */
    public void submitStream(String stream) throws Exception {
        if (this.status == 2) {
            pushToClient(this.serverIp, this.serverPort, PackMsgUtil.submitStream(this.roomId, this.userId, stream));
        } else {
            throw new RuntimeException("对方未接听，请勿调用该方法");
        }
    }

    /**
     * 服务端主动向客户端推送数据
     */
    public void pushToClient(String ip, int port, String body) {
        if (datagramSocket == null) {
            return;
        }
        try {
            byte[] buf = body.getBytes("UTF-8");
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, port));
            datagramPacket.setData(buf, 0, buf.length);
            datagramSocket.send(datagramPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (mConnectDisposable != null) {
            mConnectDisposable.dispose();
        }
        if (mHeartDisposable != null) {
            mHeartDisposable.dispose();
        }
        if (receiveMsgDis != null) {
            receiveMsgDis.dispose();
        }
        if (datagramSocket != null) {
            datagramSocket.close();
            datagramSocket.disconnect();
            datagramSocket = null;
        }
    }

    /**
     * 开始通话，会回调此接口进行通知
     */
    protected abstract void startCall();

    /**
     * 收到数据
     *
     * @param str
     */
    protected abstract void listen(String str);

    /**
     * 等待接听通知
     */
    protected abstract void callWait();
}