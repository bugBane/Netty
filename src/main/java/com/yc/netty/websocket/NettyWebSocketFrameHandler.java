package com.yc.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

public class NettyWebSocketFrameHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        // TextWebSocketFrame:表示一个文本帧
        if (o instanceof TextWebSocketFrame) {
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) o;
            String msg = textWebSocketFrame.text();
            System.out.println("服务器收到消息: " + msg);
            System.out.println(channelHandlerContext.channel().remoteAddress());
            // 回复消息
            channelHandlerContext.writeAndFlush(new TextWebSocketFrame("服务器时间" + LocalDateTime.now() + ": " + msg));
        }
    }
}
