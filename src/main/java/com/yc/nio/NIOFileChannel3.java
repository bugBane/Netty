package com.yc.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通道Channel和缓存Buffer 文件拷贝
 */
public class NIOFileChannel3 {
    public static void main(String[] args) throws Exception {
        // 获取文件输入流
        FileInputStream fis = new FileInputStream("D:\\ycPro\\src\\main\\resources\\test01.txt");
        // 获取test01文件通道
        FileChannel fileChannel = fis.getChannel();
        // 获取文件输出流
        FileOutputStream fos = new FileOutputStream("D:\\ycPro\\src\\main\\resources\\test04.txt");
        // 获取test02文件通道
        FileChannel fileChannel1 = fos.getChannel();
        // 创建缓冲区ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (true) {
            // 清理缓存区
            byteBuffer.clear();
            // 将文件通过通道读入缓冲区(缓冲区索引 0 -> 1024)
            if (fileChannel.read(byteBuffer) == -1) {
                break;
            }
            // 反转(将索引 1024 设置为 0 ) 注意！！！ 不是读和写的反转，用来配合读和写
            byteBuffer.flip();
            // 将缓冲区数据通过通道写入文件 (索引 0 -> 1024)
            fileChannel1.write(byteBuffer);
        }
        // 关闭资源
        fileChannel.close();
        fileChannel1.close();
        fos.close();
        fis.close();
    }
}
