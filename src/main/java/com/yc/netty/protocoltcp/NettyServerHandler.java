package com.yc.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

public class NettyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        int length = messageProtocol.getLength();
        byte[] bytes = messageProtocol.getData();

        // 将bytes转成字符串
        String message = new String(bytes, Charset.forName("UTF-8"));
        System.out.println("服务器接收到的长度:" + length);
        System.out.println("服务器接收到的数据:" + message);
        System.out.println("服务器接收到的消息量:" + (++this.count));

        //服务器回送给客户端一个随机id
        byte[] responseBytes = UUID.randomUUID().toString().getBytes(Charset.forName("UTF-8"));
        int responseLength = responseBytes.length;

        // 封装返回协议包
        MessageProtocol responseMessageProtocol = new MessageProtocol();
        responseMessageProtocol.setData(responseBytes);
        responseMessageProtocol.setLength(responseLength);
        channelHandlerContext.writeAndFlush(responseMessageProtocol);
    }
}
