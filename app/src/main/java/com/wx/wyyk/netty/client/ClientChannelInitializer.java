package com.wx.wyyk.netty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    private String device;
    public ClientChannelInitializer(String device) {
        this.device = device;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline p = channel.pipeline();
        p.addLast(new IdleStateHandler(20, 10, 0));
        p.addLast(new ObjectEncoder());
        p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
        p.addLast(new ReadTimeoutHandler(100));
        p.addLast(new ConnectReqHandler(device));
        p.addLast(new HeartBeatReqHandler(device));
        p.addLast(new HelpLoginMsgHandler());
    }
}
