package com.yc.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.SocketAddress;

/**
 * 心跳处理事件
 */
public class NettyHeartbeatHandler extends ChannelInboundHandlerAdapter {

    /**
     * 心跳处理
     *
     * @param ctx 上下文
     * @param evt IdleStateEvent事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 判断事件
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            SocketAddress socketAddress = ctx.channel().remoteAddress();
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println(socketAddress + "读空闲,处理...");
                    break;
                case WRITER_IDLE:
                    System.out.println(socketAddress + "写空闲,处理...");
                    break;
                case ALL_IDLE:
                    System.out.println(socketAddress + "读写空闲,处理...");
                    break;
                default:
                    break;
            }
        }
    }
}
