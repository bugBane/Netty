package com.yc.chat.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * NIO客户端
 */
public class NIOChatClient {
    // 服务端IP地址
    private final String IP = "127.0.0.1";
    // 服务端端口号
    private final int PORT = 8885;
    // 客户端通道socketChannel
    private SocketChannel socketChannel;
    // 多路复用选择器Selector
    private Selector selector;

    public NIOChatClient() {
        try {
            // 创建Selector
            selector = Selector.open();
            // 创建socketChannel
            socketChannel = SocketChannel.open(new InetSocketAddress(IP, PORT));
            // 设置非阻塞
            socketChannel.configureBlocking(false);
            // 注册socketChannel到Selector
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 客户端服务(读取)
    public void clientServer() {
        try {
            while (true) {
                if (selector.select() > 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey selectionKey = keyIterator.next();
                        // 判断读取事件
                        if (selectionKey.isReadable()) {
                            SocketChannel readChannel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            readChannel.read(byteBuffer);
                            System.out.println(new String(byteBuffer.array()));
                        }
                        keyIterator.remove();
                    }
                } else {
                    System.out.println("等待...");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 向服务器发送消息
    public void sendMessage(String message) {
        try {
            socketChannel.write(ByteBuffer.wrap(message.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
