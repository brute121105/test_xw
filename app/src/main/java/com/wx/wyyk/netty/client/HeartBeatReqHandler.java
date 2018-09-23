package com.wx.wyyk.netty.client;

import com.wx.wyyk.netty.commons.Message;
import com.wx.wyyk.netty.commons.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HeartBeatReqHandler extends SimpleChannelInboundHandler<Message> {

    private String device;

    private volatile ScheduledFuture<?> heartBeat;

    public HeartBeatReqHandler(String device) {
        this.device = device;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        //如果是心跳包
        if (message.getType() == MessageType.CONNECT_SUCCESS.getValue()) {
            //50秒钟发一个心跳
            heartBeat = ctx.executor().scheduleAtFixedRate(
                    new HeartBeatTask(ctx, this.device), 0, 50000, TimeUnit.MILLISECONDS);
        } else if (message != null &&
                message.getType() == MessageType.HEARTBEAT_RESP.getValue()) {
            System.out.println("收到服务器心跳信息");
        } else {
            //编码好的Message传递给下一个Handler
            ctx.fireChannelRead(message);
        }
    }

    //心跳包发送任务
    private class HeartBeatTask implements Runnable {

        private ChannelHandlerContext ctx;
        private String device;

        public HeartBeatTask(ChannelHandlerContext ctx, String device) {
            this.ctx = ctx;
            this.device = device;
        }

        public void run() {
            Message message = buildMessage();
            System.out.println("发送心跳信息到服务器");
            ctx.writeAndFlush(message);
        }

        private Message buildMessage() {
            Message msg = new Message();
            msg.setDevice(this.device);
            msg.setType(MessageType.HEARTBEAT_REQ.getValue());
            return msg;
        }
    }
}
