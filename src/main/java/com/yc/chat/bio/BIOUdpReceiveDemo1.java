package com.yc.chat.bio;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class BIOUdpReceiveDemo1 {
    public static void main(String[] args) throws Exception {
        // 创建udp接收客户端
        DatagramSocket datagramSocket = new DatagramSocket(8888);
        // 接收数据报
        byte[] bytes = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(bytes, 0, bytes.length);
        while (true) {
            // 接收数据报
            datagramSocket.receive(datagramPacket); //阻塞式接收
            String data = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
            if ("bye".equals(data)) {
                break;
            }
            System.out.println(data);
        }
        // 关闭资源
        datagramSocket.close();
    }
}
