package com.wx.wyyk.netty.commons;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ChannelHolder {

    private static Map<String, SocketChannel> map = new ConcurrentHashMap<>();

    public static void add(String device, SocketChannel socketChannel) {
        map.put(device, socketChannel);
    }

    public static Channel get(String device) {
        return map.get(device);
    }
    /*
    public static void remove(SocketChannel socketChannel) {
        for (Map.Entry entry : map.entrySet()) {
            if (entry.getValue() == socketChannel) {
                map.remove(entry.getKey());
            }
        }
    }
    */
    public static String remove(SocketChannel socketChannel) {
        String key = null;
        for (Map.Entry entry : map.entrySet()) {
            if (entry.getValue() == socketChannel) {
                key = entry.getKey().toString();
                map.remove(entry.getKey());
            }
        }
        return key;
    }

    /**
     * 随机获取一个channel
     * @return
     */
    public static SocketChannel randomChannel() {
        if (map.isEmpty()) {
            return null;
        }
        int rand = new Random().nextInt(map.size());
        SocketChannel[] cs = map.values().toArray(new SocketChannel[0]);
        return cs[rand];
    }

    public static Set<String> deviceList() {
        return map.keySet();
    }

    public static Collection<SocketChannel> channelList() {
        return map.values();
    }

    public static boolean isEmpty() {
        return map.isEmpty();
    }
}
