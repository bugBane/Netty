package com.yc.inet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// 不需要连接服务器
public class UdpSocketDemo1 {
    public static void main(String[] args) throws Exception {
        // 建立一个Socket，udp连接一次性
        DatagramSocket datagramSocket = new DatagramSocket();
        // 建立包
        String msg = "你好,UDP服务器";
        // 创建一个数据报文包，（信息，起始，结束，目的地址，端口号）
        DatagramPacket datagramPacket = new DatagramPacket(msg.getBytes(), 0, msg.getBytes().length, InetAddress.getByName("127.0.0.1"), 9003);
        // 发送
        datagramSocket.send(datagramPacket);
        // 关闭资源
        datagramSocket.close();
    }
}
