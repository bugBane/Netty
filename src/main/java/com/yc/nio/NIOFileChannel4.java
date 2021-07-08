package com.yc.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * 通道Channel transform 文件拷贝
 */
public class NIOFileChannel4 {
    public static void main(String[] args) throws Exception {
        // 文件输入流
        FileInputStream fis = new FileInputStream("D:\\ycPro\\src\\main\\resources\\a2.png");
        // 文件输出流
        FileOutputStream fos = new FileOutputStream("D:\\ycPro\\src\\main\\resources\\a3.png");
        // 文件输入流通道
        FileChannel fisChannel = fis.getChannel();
        // 文件输出流通道
        FileChannel fosChannel = fos.getChannel();
        // 通道transform拷贝(目的通道.transform(原通道))
        fosChannel.transferFrom(fisChannel, 0, fisChannel.size());
        // 关闭资源
        fosChannel.close();
        fisChannel.close();
        fos.close();
        fis.close();
    }
}
