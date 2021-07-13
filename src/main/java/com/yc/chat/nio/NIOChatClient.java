package com.yc.chat.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

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
                // 判断事件发生(select方法为阻塞的,如果一直没有事件发生,那么客户端就会阻塞在这里)
                if (selector.select() > 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey selectionKey = keyIterator.next();
                        // 判断读取事件
                        if (selectionKey.isReadable()) {
                            SocketChannel readChannel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            readChannel.read(byteBuffer);
                            System.out.println(readChannel.getRemoteAddress() + ":" + new String(byteBuffer.array()));
                        }
                        keyIterator.remove();
                    }
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
            System.out.println(socketChannel.getRemoteAddress() + ":" + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 客户端程序入口
    public static void main(String[] args) {
        // 程序主方法接收服务端消息
        final NIOChatClient chatClient = new NIOChatClient();
        //启动一个线程, 每隔3秒，读取从服务器发送数据
        new Thread() {
            public void run() {
                while (true) {
                    chatClient.clientServer();
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        // 控制台输入信息
        Scanner scanner = new Scanner(System.in);
        // 主线程发送消息到服务端
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            chatClient.sendMessage(line);
        }
    }

}