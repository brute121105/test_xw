package com.wx.wyyk.netty.client;

import com.wx.wyyk.netty.commons.Message;
import com.wx.wyyk.netty.commons.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ConnectReqHandler extends SimpleChannelInboundHandler<Message> {

    private String device;
    private String assistant;

    public ConnectReqHandler(String device, String assistant) {
        this.device = device;
        this.assistant = assistant;
    }

    //三次握手成功,发送登录验证
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(this.device + "doAction发起连接请求...");
        ctx.writeAndFlush(buildMessage());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        //登录验证失败
        if (message.getType() == MessageType.CONNECT_FAIL.getValue()) {
            System.out.println(this.device + "doAction连接失败...");
            ctx.close();
        } else if (message.getType() == MessageType.CONNECT_SUCCESS.getValue()) {//登录验证成功
            System.out.println(this.device + "doAction连接成功...");
            ctx.fireChannelRead(message);
        } else {
            ctx.fireChannelRead(message);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }

    private Message buildMessage() {
        Message message = new Message();
        message.setDevice(this.device);
        message.setAssistant(this.assistant);
        message.setType(MessageType.CONNECT_REQ.getValue());
        return message;
    }
}
