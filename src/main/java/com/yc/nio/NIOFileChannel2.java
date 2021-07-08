package com.yc.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通道Channel和缓存Buffer 从文件读出到控制台
 */
public class NIOFileChannel2 {
    public static void main(String[] args) throws Exception {
        // 获取文件
        File file = new File("D:\\ycPro\\src\\main\\resources\\test03.txt");
        FileInputStream fis = new FileInputStream(file);
        // 根据文件输入流获取通道
        FileChannel fileChannel = fis.getChannel();
        // 创建一个缓冲区（可双向），类比new byte[1024]
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        // 通过通道将文件写入buffer中
        fileChannel.read(byteBuffer);
        // 打印到控制台
        System.out.println(new String(byteBuffer.array()));

        // 经过NIOMapperByteBuffer之后的改进
        // FileInputStream为文件输入流,只能读不是双向的，但是FileChannel.MapMode.READ_ONLY可以获取只读，所以没有问题
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        while(mappedByteBuffer.hasRemaining()){
            System.out.print((char) mappedByteBuffer.get());
        }
        System.out.println("改进完成");

        // 关闭资源
        fileChannel.close();
        fis.close();
    }
}
