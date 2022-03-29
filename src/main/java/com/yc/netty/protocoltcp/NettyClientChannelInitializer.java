package com.yc.netty.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class NettyClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 发送数据的协议包编码
        pipeline.addLast(new MessageEncoder());
        // 回送数据的协议包解码
        pipeline.addLast(new MessageDecoder());
        // 业务处理
        pipeline.addLast(new NettyClientHandler());
    }
}
