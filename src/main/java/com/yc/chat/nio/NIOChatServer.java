package com.yc.chat.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO群聊服务器
 */
public class NIOChatServer {
    // 服务端端口号
    private final int PORT = 8885;
    // 多路复用选择器Selector
    private Selector selector;
    // 服务端通道
    private ServerSocketChannel serverSocketChannel;

    // 服务端构造器
    public NIOChatServer() {
        try {
            // 创建Selector
            selector = Selector.open();
            // 创建服务端serverSocketChannel
            serverSocketChannel = ServerSocketChannel.open();
            // 指定服务器端口号并启动
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            // 设置非阻塞
            serverSocketChannel.configureBlocking(false);
            // serverSocketChannel注册到Selector
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 服务端服务功能
    public void serverService() {
        try {
            while (true) {
                // 判断事件发生(select方法为阻塞的,如果一直没有事件发生,那么服务端就会阻塞在这里)
                if (selector.select() > 0) {
                    // 获取发生事件的selectionKey集合
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    // 迭代器遍历集合(remove,否则并发修改异常)
                    Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey selectionKey = keyIterator.next();
                        // 判断客户端连接
                        if (selectionKey.isAcceptable()) {
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            // 客户端设置非阻塞
                            socketChannel.configureBlocking(false);
                            // socketChannel注册到Selector
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            // 提示客户端上线
                            System.out.println(socketChannel.getRemoteAddress() + ":已经上线");
                        }
                        // 判断客户端发送数据
                        if (selectionKey.isReadable()) {
                            // 读取客户端数据
                            readMessage(selectionKey);
                        }
                        // 事件处理完成需要手动删除，防止重复处理
                        keyIterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 读取客户端数据
    private void readMessage(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            // 获取该客户端socketChannel
            socketChannel = (SocketChannel) selectionKey.channel();
            // 获取该客户端byteBuffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            // 读取客户端数据
            int read = socketChannel.read(byteBuffer);
            // 客户端传值
            if (read > 0) {
                // 读取客户端数据
                String message = new String(byteBuffer.array());
                System.out.println(socketChannel.getRemoteAddress() + ":" + message);
                // 转发该客户端数据到其他客户端
                // socketChannel(正确)
                transformMessageToOtherClient(message, socketChannel);
                // selectionKey(错误)
//                transformMessageToOtherClient(message, selectionKey);
            }
        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + ":已经离线");
                // 撤销注册selectionKey
                selectionKey.cancel();
                //关闭通道
                socketChannel.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    // 转发该客户端数据到其他客户端
    private void transformMessageToOtherClient(String message, SocketChannel socketChannel) {
        // 获取所有注册的selectionKey
        Set<SelectionKey> keys = selector.keys();
        // 遍历keys
        for (SelectionKey key : keys) {
            Channel targetChannel = key.channel();
            // 排除自己(先判断实例是否相同类型，再判断是否是同一个对象地址)
            if (targetChannel instanceof SocketChannel && targetChannel != socketChannel) {
                // 发送数据给客户端
                try {
                    ((SocketChannel) targetChannel).write(ByteBuffer.wrap(message.getBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 转发该客户端数据到其他客户端(错误)
    private void transformMessageToOtherClient(String message, SelectionKey selectionKey) {
        // 获取所有注册的selectionKey
        Set<SelectionKey> keys = selector.keys();
        // 遍历keys
        for (SelectionKey key : keys) {
            // 排除自己(先判断实例是否相同类型，再判断是否是同一个对象地址)
            if (key instanceof SelectionKey && selectionKey != key) {
                Channel socketChannel = key.channel();
                // 发送数据给客户端
                try {
                    ((SocketChannel) socketChannel).write(ByteBuffer.wrap(message.getBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 服务端入口
    public static void main(String[] args) {
        NIOChatServer chatServer = new NIOChatServer();
        chatServer.serverService();
    }
}
