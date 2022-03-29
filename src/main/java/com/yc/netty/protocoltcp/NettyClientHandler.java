package com.yc.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class NettyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 使用客户端发送1000条Hello,World
        // --使用协议包进行优化,防止粘包问题影响数据
        for (int i = 0; i < 1000; i++) {
            String data = "Hello,World" + i;
            byte[] bytes = data.getBytes(Charset.forName("UTF-8"));
            int length = bytes.length;
            // 封装协议包
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setData(bytes);
            messageProtocol.setLength(length);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        // 服务端给客户端的回送数据
        int length = messageProtocol.getLength();
        byte[] bytes = messageProtocol.getData();

        // 将bytes转成字符串
        String message = new String(bytes, Charset.forName("UTF-8"));
        System.out.println("客户端接收到的长度:" + length);
        System.out.println("客户端接收到的数据:" + message);
        System.out.println("客户端接收到的消息量:" + (++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
