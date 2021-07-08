package com.yc.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * RandomAccessFile既可以读取文件内容，也可以向文件输出数据。同时，RandomAccessFile支持“随机访问”的方式，程序快可以直接跳转到文件的任意地方来读写数据。需要预知RandomAccessFile知识可以看思路
 * MappedByteBuffer可以让文件直接在内存(堆外内存)修改，操作系统不需要拷贝一次
 */
public class NIOMapperByteBuffer {
    public static void main(String[] args) throws Exception {
        //    "r" : 以只读方式打开。调用结果对象的任何 write 方法都将导致抛出 IOException。
        //    "rw": 打开以便读取和写入。
        //    "rws": 打开以便读取和写入。相对于 "rw"，"rws" 还要求对“文件的内容”或“元数据”的每个更新都同步写入到基础存储设备。
        //    "rwd" : 打开以便读取和写入，相对于 "rw"，"rwd" 还要求对“文件的内容”的每个更新都同步写入到基础存储设备。
        RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\ycPro\\src\\main\\resources\\test03.txt", "rw");
        FileChannel randomAccessFileChannel = randomAccessFile.getChannel();
        // 参数1：FileChannel.MapMode.READ_WRITE代表使用的读写模式，可以与NIOFileChannel1和NIOFileChannel2案例配合看，RandomAccessFile既可以读又可以写，所以可以使用读写模式
        // 参数2：0(可以修改的起始位置)
        // 参数3：5(映射到内存的大小，注意不是索引位置) 可以直接修改的范围就是0-5
        MappedByteBuffer mappedByteBuffer = randomAccessFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        // 起始索引为0，所以索引<内存大小5
        mappedByteBuffer.put(0, (byte) 'a');
        mappedByteBuffer.put(3, (byte) 'B');
        // 文件修改位置包左不包右:[0,5) 注意:起始索引为0 而索引必须小于<内存大小，所以报错:IndexOutOfBoundsException
//        mappedByteBuffer.put(5, (byte) 1);
        MappedByteBuffer mappedByteBuffer1 = randomAccessFileChannel.map(FileChannel.MapMode.READ_WRITE, 2, 6);
        // 文件修改位置就是[2,7), 但是索引(起始索引为0)<内存大小，5<6,所以不报错
        mappedByteBuffer1.put(5, (byte) 1);
        // 关闭资源
        randomAccessFileChannel.close();
        randomAccessFile.close();
    }
}
