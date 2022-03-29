package com.yc.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO服务器
 */
public class NIOServer {
    public static void main(String[] args) throws Exception {
        // 创建通道ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 创建多路复用选择器Selector
        Selector selector = Selector.open();
        // 绑定端口8887到ServerSocket，并启动
        serverSocketChannel.socket().bind(new InetSocketAddress(8887));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 将ServerSocketChannel注册到Selector,事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 等待客户端连接
        while (true) {
            // 等待1s，如果没有事件发生返回
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1s，无连接");
                continue;
            }
            // keys所有注册了的SelectionKey集合，selectedKeys是所有有事件就绪的SelectionKey集合
            Set<SelectionKey> keys = selector.selectedKeys();
            // 注意不要使用foreach方法，用迭代器，需要remove key,否则会有并发修改异常
            Iterator<SelectionKey> keyIterator = keys.iterator();
            // 遍历集合
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                // 根据key对相应的通道进行处理
                // 判断是否为OP_ACCEPT(只要发生连接事件，那么就说明有客户端连接，此时去接收客户端就可以)
                if (selectionKey.isAcceptable()) {
                    // accept方法虽然没有连接会阻塞，但是因为之前已经判断事件isAcceptable,所以一定有连接就不会存在阻塞
                    // 通过服务器通道获取连接的客户端通道
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 设置非阻塞
                    socketChannel.configureBlocking(false);
                    // 将客户端通道socketChannel注册到Selector,事件为OP_READ,同时给socketChannel关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                // 判断是否为OP_READ(需要根据对应的事件key得到对应的socketChannel)
                if (selectionKey.isReadable()) {
                    // 通过key反推对应channel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    // 获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                    // 如果客户端关闭那么这里将会报错IOException
                    // 因为通道没有关闭但是无法读取信息了,需要可以抓取异常进行判断 详细可以查看NIOChatServer的离线提醒
                    socketChannel.read(buffer);
                    System.out.println("from 客户端 " + new String(buffer.array()));
                }
                // 手动从集合中移动当前的selectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }


}
