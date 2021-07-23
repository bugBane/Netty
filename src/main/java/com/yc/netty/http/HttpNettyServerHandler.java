package com.yc.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * SimpleChannelInboundHandler 是 ChannelInboundHandler 子类
 * HttpObject 客户端和服务端相互通讯的数据被封装成HttpObject
 */
public class HttpNettyServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 读取客户端数据
     *
     * @param channelHandlerContext 上下文对象,含有管道pipeline、通道channel、地址...
     * @param httpObject            客户端和服务端相互通讯的数据
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        System.out.println("--------------------------------");
        // 判断是不是HttpRequest请求
        if (httpObject instanceof HttpRequest) {
            System.out.println("httpObject：" + httpObject);
            System.out.println("--------------------------------");
            System.out.println("客户端地址:" + channelHandlerContext.channel().remoteAddress());
            // 转换成HttpRequest
            HttpRequest httpRequest = (HttpRequest) httpObject;
            // 获取uri
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico,不做响应");
                return;
            }

            // 回复信息给浏览器(Http协议)
            ByteBuf byteBuf = Unpooled.copiedBuffer("Hello,我是服务端", CharsetUtil.UTF_16);
            // 构造一个Http响应(HttpResponse)
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            // 将response返回
            channelHandlerContext.writeAndFlush(response);
        }
    }
}
