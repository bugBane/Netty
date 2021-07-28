package com.yc.chat.netty.group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map;

public class NettyChatServerHandler extends SimpleChannelInboundHandler<String> {

    // GlobalEventExecutor.INSTANCE 是全局的事件执行器,单例(工具类)
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    // 私聊功能
    private static Map<String, Channel> channemMap = new HashMap<>();

    // 定义一个channel组,管理所有的channel--上边channelGroup为netty自带的处理channel的工具类
//    public static List<Channel> channels = new ArrayList<Channel>();

    // 表示建立连接
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客户端加入聊天的信息推送给其他在线的客户端
        // writeAndFlush:该方法会将channelGroup中所有的channel遍历并发送消息,无需自己遍历
        channelGroup.writeAndFlush("客户端" + channel.remoteAddress() + " 加入聊天");
        channelGroup.add(channel);
        // 私聊功能 举例 具体可以使用userId
        channemMap.put("userId", channel);
    }

    // channnel处于活动状态(服务端与客户端连接成功),提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了");
    }

    // channel处于非活动状态(服务端与客户端连接失败),提示xx离线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线了");
    }

    // 表示断开连接
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("客户端" + channel.remoteAddress() + " 离开聊天");
        System.out.println("channelGroup size: " + channelGroup.size());
    }

    // 读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        // 获取到当前channnel
        Channel channel = channelHandlerContext.channel();
        // 注意String s是经过解码和编码过的(服务端处理器StringDecoder和StringEncoder),否则需要Object s来进行手动编码和解码而不能直接使用String(去掉编码和解码处理器直接用String接收,服务端接收不到)
        // 客户端和服务端都需要StringDecoder和StringEncoder,否则都需要使用Object s转Byte buf来进行接收和发送消息
        // 这时我们遍历channelGroup,根据不同的情况,回送不同的信息
        channelGroup.forEach(socketChannel -> {
            // 群聊
            if (channel != socketChannel) {
                // 给其他客户端转发
                socketChannel.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送了消息: " + s + "\n");
            } else {
                // 给自己提示
                socketChannel.writeAndFlush("[自己]" + channel.remoteAddress() + " 发送了消息: " + s + "\n");
            }
            // 可以扩展私聊功能
        });
    }

}
