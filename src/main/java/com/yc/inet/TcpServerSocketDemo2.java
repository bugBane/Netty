package com.yc.inet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerSocketDemo2 {
    public static void main(String[] args) throws Exception {
        // 服务器基本操作
        ServerSocket serverSocket = new ServerSocket(9001);
        Socket socket = serverSocket.accept();
        InputStream is = socket.getInputStream();
        // 接收文件
        FileOutputStream fos = new FileOutputStream(new File("D:\\ycPro\\src\\main\\resources\\test02.txt"));
        byte[] bytes = new byte[1024];
        int len;
        while ((len = is.read(bytes)) != -1) {
            fos.write(bytes, 0, len);
        }
        // 往客户端返回信息
        OutputStream os = socket.getOutputStream();
        os.write("接收完毕，over".getBytes());
        // 关闭资源
        os.close();
        fos.close();
        is.close();
        socket.close();
        serverSocket.close();
    }
}
