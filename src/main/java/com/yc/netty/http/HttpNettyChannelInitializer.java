package com.yc.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpNettyChannelInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("HttpServerCodec", new HttpServerCodec());
        pipeline.addLast("HttpNettyServerHandler", new HttpNettyServerHandler());
    }
}
