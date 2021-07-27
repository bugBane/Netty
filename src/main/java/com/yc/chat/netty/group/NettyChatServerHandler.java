package com.yc.chat.netty.group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyChatServerHandler extends SimpleChannelInboundHandler<String> {

    // GlobalEventExecutor.INSTANCE 是全局的事件执行器,单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 定义一个channel组,管理所有的channel--上边channelGroup为netty自带的处理channel的类
//    public static List<Channel> channels = new ArrayList<Channel>();

    // 表示建立连接
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客户端加入聊天的信息推送给其他在线的客户端
        // writeAndFlush:该方法会将channelGroup中所有的channel遍历并发送消息,无需自己遍历
        channelGroup.writeAndFlush("客户端" + channel.remoteAddress() + "加入聊天");
        channelGroup.add(channel);
    }

    // channnel处于活动状态(客户端连接成功),提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了");
    }

    // channel处于非活动状态(客户端连接失败),提示xx离线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了");
    }

    // 表示断开连接
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("客户端" + channel.remoteAddress() + "离开聊天");
        System.out.println("channelGroup size" + channelGroup.size());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
    }

}
