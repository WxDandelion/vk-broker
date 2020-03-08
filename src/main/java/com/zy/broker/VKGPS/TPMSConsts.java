package com.zy.broker.VKGPS;

import java.nio.charset.Charset;

public class TPMSConsts {
    public static final String string_encoding = "GBK";

    public static final Charset string_charset = Charset.forName(string_encoding);
    // 标识位
    public static final int pkg_delimiter = 0x7e;
    // 客户端发呆15分钟后,服务器主动断开连接
    public static int tcp_client_idle_minutes = 30;


    // 后期应该将两项进行整合
    /*
     * 功能类型编码
     */
    // 上传状态类
    public static final String msg_upload_status = "A";
    // 上传定位类
    public static final String msg_upload_loc = "B";
    // 动态加载类
    public static final String msg_dynamical_loading = "D";
    // OBD信息
    public static final String msg_obd = "O";


    /*
     * 功能项关键字
     */
    // 终端心跳
    public static final String key_heartbeat = "H";
    // 警情上报
    public static final String key_warning = "A";
    // 定时回传定位信息
    public static final String key_loc = "A";

}
