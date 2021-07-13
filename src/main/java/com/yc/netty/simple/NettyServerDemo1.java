package com.yc.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class NettyServerDemo1 {
    public static void main(String[] args) throws Exception {
        // 创建BossGroup和WorkGroup(都是NioEventLoopGroup)
        // BossGroup:用来处理客户端连接(封装为NioEventLoop)，将连接的客户端(NioSocketChannel)注册到WorkGroup(封装为NioEventLoop)
        // WorkGroup:用来处理客户端read、write、业务(NioEventLoop)
        // 两个都是无限循环(处理任务队列)
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        // 创建服务端的启动对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 配置参数:使用链式编程进行设置
        serverBootstrap.group(bossGroup, workGroup) // 设置两个EventLoopGroup
                .channel(NioServerSocketChannel.class) // 使用NioServerSocketChannel作为服务端通道
                .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到的连接数
                .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() {// 创建客户端通道测试对象(匿名对象的方式)
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 给pipeline设置处理器
                        socketChannel.pipeline().addLast(null);
                    }
                });

        // 绑定一个端口号并且同步，生成一个channelFuture对象
        // 启动服务端
        ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(9998)).sync();

        System.out.println("服务端启动");

        // 对关闭通道进行监听
        channelFuture.channel().closeFuture().sync();
    }
}
