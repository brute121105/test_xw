package com.wx.wyyk.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    // 服务端ip
    private String host = "127.0.0.1";
    // 服务端端口
    private int port = 8000;
    // 设备编号
    private String device;
    private String assistant;// 辅助号，空表示该设备不辅助

    public NettyClient(String host, int port, String device, String assistant) {
        this.host = host;
        this.port = port;
        this.device = device;
        this.assistant = assistant;
    }

    public void connect() throws Exception {
        try {
            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap bs = new Bootstrap();
            bs.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ClientChannelInitializer(this.device, this.assistant));
            System.out.println("doAction开始连接...");
            ChannelFuture future = bs.connect(new InetSocketAddress(host, port)).sync();
            future.channel().closeFuture().sync();//这一步会阻塞住
            System.out.println("doAction关闭");
        } finally {
            //断线重连
            executor.execute(new Runnable() {
                public void run() {
                    System.out.println("doAction尝试重新连接...");
                    //等待InterVAl时间，重连
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        //发起重连
                        connect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws Exception {
        NettyClient client = new NettyClient("127.0.0.1", 8000, "A001", "fz2018801");
        client.connect();
    }
}
