package com.wx.wyyk.netty.commons;

import java.io.Serializable;

/**
 * 辅助登录的消息
 */
public class HelpLoginMsg implements Serializable {

    private Long id;// 唯一标识
    private String phone;// 登录的号码
    private String code;// 验证码
    //private String helpWx;// 辅助登录的号码

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "HelpLoginMsg{" +
                "id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
