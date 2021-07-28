package com.yc.chat.netty.group;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyChatServer {
    // 服务端端口号
    private final int PORT;

    public NettyChatServer(int port) {
        this.PORT = port;
    }

    public void run() throws Exception {
        // 单线程
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 8线程 默认2*核数
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);
        try {
            // 创建服务端启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 设置参数
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 获取管道
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 加入相关handler
                            // 解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            // 编码器
                            pipeline.addLast("encoder", new StringEncoder());
                            // 加入自定义handler
                            pipeline.addLast(new NettyChatServerHandler());
                        }
                    });
            // 启动服务端
            ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();
            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyChatServer(9997).run();
    }
}
