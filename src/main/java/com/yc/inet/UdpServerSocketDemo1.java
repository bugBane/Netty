package com.yc.inet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

// 还需要等待客户端连接
public class UdpServerSocketDemo1 {
    public static void main(String[] args) throws Exception {
        // 开放端口
        DatagramSocket datagramSocket = new DatagramSocket(9003);
        // 接收数据包
        byte[] bytes = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(bytes, 0, bytes.length);
        // 接收
        datagramSocket.receive(datagramPacket);// 阻塞接收
        System.out.println(datagramPacket.getAddress());
        System.out.println(new String(datagramPacket.getData()));
        // 关闭资源
        datagramSocket.close();
    }
}
