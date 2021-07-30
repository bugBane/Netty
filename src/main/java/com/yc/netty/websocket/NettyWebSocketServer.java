package com.yc.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;

public class NettyWebSocketServer {
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
                            // 基于http协议使用http编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 以块的方式写,添加ChunkedWriteHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            // http传输过程中会将数据进行分段,HttpObjectAggregator可以将多个段聚合(这就是为什么浏览器发送大量数据时会发出多个http请求)
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            // 对应websocket,它的数据是以帧(frame)形式传递,websocket的uri为 ws://localhost:8887/hello
                            // WebSocketServerProtocolHandler核心功能是将http协议升级为websocket协议,保持长连接
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            // 自定义handler
                            pipeline.addLast(new NettyWebSocketFrameHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(8887)).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
