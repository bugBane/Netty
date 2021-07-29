package com.yc.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * netty 心跳机制 非阻塞io
 */
public class NettyHeartbeatServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 1.netty提供 IdleStateHandler:
                            //      long readerIdleTime, 长时间没有读就会发送一个心跳检测包检测是否连接
                            //      long writerIdleTime, 长时间没有写就会发送一个心跳检测包检测是否连接
                            //      long allIdleTime,    长时间没有读写就会发送一个心跳检测包检测是否连接
                            //      TimeUnit unit        时间单位
                            // 2.当IdleStateHandler触发后,就会传递给管道的下一个handler去处理,通过调用下一个handler的userEventTiggered方法来处理IdleStateEvent
                            pipeline.addLast(new IdleStateHandler(2, 4, 6, TimeUnit.SECONDS));
                            // 自定义handler--实现userEventTiggered处理IdleStateEvent
                            pipeline.addLast(new NettyHeartbeatHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(8888)).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
