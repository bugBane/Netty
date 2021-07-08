package com.yc.chat.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class BIOUdpSenderDemo1 {
    public static void main(String[] args) throws Exception {
        // 创建udp客户端
        DatagramSocket datagramSocket = new DatagramSocket(6666);
        // 从控制台输入数据
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String s = br.readLine();
            // 将数据包装到数据报
            DatagramPacket datagramPacket = new DatagramPacket(s.getBytes(), 0, s.getBytes().length, new InetSocketAddress("127.0.0.1", 8888));
            // 发送数据报
            datagramSocket.send(datagramPacket);
            if ("bye".equals(s)) {
                break;
            }
        }
        // 关闭资源
        datagramSocket.close();
    }
}
