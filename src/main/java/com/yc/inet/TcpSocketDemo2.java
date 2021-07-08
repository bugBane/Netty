package com.yc.inet;

import java.io.*;
import java.net.Socket;

public class TcpSocketDemo2 {
    public static void main(String[] args) throws Exception {
        // 客户端基本操作
        Socket socket = new Socket("127.0.0.1", 9001);
        OutputStream os = socket.getOutputStream();
        // 传输文件
        FileInputStream fis = new FileInputStream(new File("D:\\ycPro\\src\\main\\resources\\test01.txt"));
        byte[] bytes = new byte[1024];
        int len;
        while ((len = fis.read(bytes)) != -1) {
            os.write(bytes, 0, len);
        }
        // 发送完毕之后需要告诉服务器客户端写入完毕
        socket.shutdownOutput();
        // 接收服务端信息
        InputStream is = socket.getInputStream();
        // 管道接收
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = is.read(bytes)) != -1) {
            baos.write(bytes, 0, len);
        }
        System.out.println(baos.toString());
        // 关闭资源
        baos.close();
        is.close();
        fis.close();
        os.close();
        socket.close();
    }
}
