package com.wx.wyyk.netty.commons;

public enum MessageType {
    CONNECT_REQ((byte) 1), // 客户端连接请求
    CONNECT_SUCCESS((byte) 2), // 客户端连接成功
    CONNECT_FAIL((byte) 3), // 客户端连接失败
    HEARTBEAT_REQ((byte) 4), // 心跳请求
    HEARTBEAT_RESP((byte) 5), // 心跳响应
    HELP_LOGIN((byte) 6); // 辅助登录

    private byte value;

    MessageType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
}
