package com.wx.wyyk.netty.commons;

import java.io.Serializable;

public class Message implements Serializable {

    private String device;// 设备编号
    private byte type;// 消息类型
    private Object body;

    private static final long serialVersionUID = 1L;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "device='" + device + '\'' +
                ", type=" + type +
                ", body=" + body +
                '}';
    }
}
