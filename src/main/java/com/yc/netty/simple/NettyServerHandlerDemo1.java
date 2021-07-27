package com.yc.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 自定义一个Handler需要继承 某个HandlerAdapter
 * 用来处理客户端请求(业务/read/write/...)
 */
public class NettyServerHandlerDemo1 extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据(客户端发送的信息)
     *
     * @param ctx 上下文对象,含有管道pipeline、通道channel、地址...
     * @param msg 客户端发送的消息,默认Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("server ChannelHandlerContext:" + ctx);
        ByteBuf byteBuf = (ByteBuf) msg;
        // 加入taskQueue
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("服务端处理线程" + Thread.currentThread().getName());
                    System.out.println("客户端发送的消息是:" + byteBuf.toString(CharsetUtil.UTF_8));
                    System.out.println("客户端地址是:" + ctx.channel().remoteAddress());
                    System.out.println("channel:"+ctx.channel());
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 数据读取完毕
     *
     * @param ctx 上下文对象,含有管道pipeline、通道channel、地址...
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入到缓存并刷新(一般来说，我们对这个发送的数据进行编码)
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello World Server", CharsetUtil.UTF_8));
    }

    /**
     * 一般来说,出现异常需要关闭通道
     *
     * @param ctx   上下文对象,含有管道pipeline、通道channel、地址...
     * @param cause 异常信息
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭资源
        ctx.close();
    }
}
