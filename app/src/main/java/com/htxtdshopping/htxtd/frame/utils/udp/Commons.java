package com.htxtdshopping.htxtd.frame.utils.udp;

//指定udp传输信号标记
public class Commons {
    // 1：开始启动客户端udp监听时，上报完地址就默认该客户端已经启动了监听，可以开始传输数据
    //完整报文格式：  SUBMIT_ADDRESS|roomId|userId
    public static final String SUBMIT_ADDRESS = "SUBMIT_ADDRESS";

    //2：上报数据流
    //完整报文格式：  SUBMIT_STREAM|roomId|userId|START|数据流字符串|END
    public static final String SUBMIT_STREAM = "SUBMIT_STREAM";

    //3：服务端响应数据流
    //完整报文格式：  RESPONSE_STREAM|START|数据流字符串|END
    public static final String RESPONSE_STREAM = "RESPONSE_STREAM";

    //等待接听
    //完整报文格式：  RESPONSE_WAIT
    public static final String RESPONSE_WAIT = "RESPONSE_WAIT";
    //开始通话
    //完整报文格式：  START
    public static final String START = "START";
    //挂断通知
    //完整报文格式：  CALL_STOP
    public static final String CALL_STOP_RES = "CALL_STOP_RES";
    //客户端挂断
    //完整报文格式：  CALL_STOP|房间号|用户id
    public static final String CALL_STOP_REQ = "CALL_STOP_REQ";

    //客户端发心跳
    //完整报文格式：  PING|房间号|用户id
    public static final String PING = "PING";

    //服务端发心跳
    //完整报文格式：  PONG|房间号|用户id
    public static final String PONG = "PONG";
}