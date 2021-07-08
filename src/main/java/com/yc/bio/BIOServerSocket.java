package com.yc.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO服务器
 */
public class BIOServerSocket {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(6666);
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            // 监听等待客户连接 阻塞 TCP
            Socket socket = serverSocket.accept();
            // 连接上以后就创建一个线程与之交互
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    InputStream is = null;
                    try {
                        // io阻塞
                        is = socket.getInputStream();
                        byte[] bytes = new byte[1024];
                        int len;
                        while (true) {
                            if ((len = is.read(bytes)) != -1) {
                                System.out.println(Thread.currentThread().getName() + ":" + new String(bytes, 0, len));
                            } else {
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (is != null) {
                                is.close();
                            }
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
