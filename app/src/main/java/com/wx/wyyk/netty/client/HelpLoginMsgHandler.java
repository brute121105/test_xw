package com.wx.wyyk.netty.client;

import com.wx.wyyk.netty.commons.HelpLoginMsg;
import com.wx.wyyk.netty.commons.Message;
import com.wx.wyyk.netty.commons.MessageType;

import hyj.xw.cache.CacheMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HelpLoginMsgHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        if (message.getType() == MessageType.HELP_LOGIN.getValue()) {
            HelpLoginMsg helpLoginMsg = (HelpLoginMsg) message.getBody();
            System.out.println("doAction-->收到辅助登录消息: " + helpLoginMsg);
            CacheMsg.addHelpLoginMsg(helpLoginMsg);
            System.out.println("doAction-->收到辅助登录消息长度: " + CacheMsg.helpLoginMsgs.size());
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {

    }
}
