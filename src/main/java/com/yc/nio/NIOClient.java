package com.yc.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * NIO客户端
 */
public class NIOClient {
    public static void main(String[] args) throws Exception {
        // 创建socket客户端通道
        SocketChannel socketChannel = SocketChannel.open();
        // 设置非阻塞
        socketChannel.configureBlocking(false);
        // 创建服务器端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8887);
        // 连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("长时间没有连接到服务器，但是客户端没有阻塞可以进行其他操作");
            }
        }
        // 连接成功后发送数据
        String s = "Hello,World";
//        ByteBuffer byteBuffer = ByteBuffer.wrap(s.getBytes());
        ByteBuffer byteBuffer = ByteBuffer.allocate(s.length());
        byteBuffer.put(s.getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
