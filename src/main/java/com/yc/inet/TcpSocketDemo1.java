package com.yc.inet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 客户端
 */
public class TcpSocketDemo1 {
    public static void main(String[] args) {
        Socket socket = null;
        OutputStream os = null;
        try {
            // 根据 目的ip，端口号创建一个socket连接
            socket = new Socket("127.0.0.1", 9000);
            // 发送信息 io流
            os = socket.getOutputStream();
            os.write("Hello World ServerSocket".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
