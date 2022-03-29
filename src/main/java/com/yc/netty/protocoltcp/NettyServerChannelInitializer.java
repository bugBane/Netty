package com.yc.netty.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 接收数据的协议包解码
        pipeline.addLast(new MessageDecoder());
        // 回送数据的协议包编码
        pipeline.addLast(new MessageEncoder());
        pipeline.addLast(new NettyServerHandler());
    }
}
