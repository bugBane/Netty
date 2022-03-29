package com.yc.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 使用客户端发送10条Hello,World
        for (int i = 0; i < 1000; i++) {
            ByteBuf byteBuf = Unpooled.copiedBuffer("Hello,World" + i, Charset.forName("UTF-8"));
            ctx.writeAndFlush(byteBuf);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        // 将bytes转成字符串
        String message = new String(bytes, Charset.forName("UTF-8"));
        System.out.println("客户端接收到的数据:" + message);
        System.out.println("客户端接收到的消息量:" + (++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
