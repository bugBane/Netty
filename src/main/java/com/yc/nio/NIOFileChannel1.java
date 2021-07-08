package com.yc.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通道Channel和缓存Buffer 从字符串放入文件
 */
public class NIOFileChannel1 {
    public static void main(String[] args) throws Exception {
        // 获取文件
        FileOutputStream fos = new FileOutputStream("D:\\ycPro\\src\\main\\resources\\test03.txt");
        // 根据文件输出流获取通道
        FileChannel fileChannel = fos.getChannel();
        String s = "Hello,NIOFileChannel";
        // 创建一个缓冲区（可双向），类比new byte[1024]
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 将s放入缓冲区
        byteBuffer.put(s.getBytes());
        // 对byteBuffer进行反转，让通道读取
        byteBuffer.flip();
        // 通过通道将byteBuffer写出到文件
        fileChannel.write(byteBuffer);

        // 经过NIOMapperByteBuffer之后的改进,但是会发现报错：NonReadableChannelException
        // FileOutputStream为文件输出流,只能写不能读不是双向的，所以FileChannel.MapMode.READ_WRITE获取读是有问题的
//        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
//        String s1 = "1Hello,NIOFileChannel1";
//        mappedByteBuffer.put(s1.getBytes());

        // 关闭资源
        fileChannel.close();
        fos.close();
    }
}
