package com.yc.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

public class NettyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        // 将bytes转成字符串
        String message = new String(bytes, Charset.forName("UTF-8"));
        System.out.println("服务器接收到的数据:" + message);
        System.out.println("服务器接收到的消息量:" + (++this.count));

        //服务器回送给客户端一个随机id
        ByteBuf responseBuf = Unpooled.copiedBuffer(UUID.randomUUID().toString(), Charset.forName("UTF-8"));
        channelHandlerContext.writeAndFlush(responseBuf);
    }
}
