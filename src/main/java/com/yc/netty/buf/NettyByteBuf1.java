package com.yc.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf1 {
    public static void main(String[] args) {
        // 创建一个ByteBuf 该对象包含一个byte[10]数组
        // 在netty的buffer中,不需要使用flip进行反转,底层维护了readerIndex和writeIndex
        // 通过readerIndex、writerIndex和capacity将buffer划分为三个区域:    0 ~ readerIndex:已经读取的部分;   readerIndex ~ writerIndex:可以读取的部分;     writerIndex ~ capacity:可以写入的部分
        ByteBuf byteBuf = Unpooled.buffer(10);
        // writerIndex++
        for (int i = 0; i < 10; i++) {
            byteBuf.writeByte(i);
        }
        // 容量 10
        System.out.println(byteBuf.capacity());
        // 输出
        for (int i = 0; i < byteBuf.capacity(); i++) {
            System.out.println(byteBuf.getByte(i));
        }
        // readerIndex++
        for (int i = 0; i < byteBuf.capacity(); i++) {
            System.out.println(byteBuf.readByte());
        }
        System.out.println("读取完毕");
        System.out.println(byteBuf.array());
        System.out.println(byteBuf.readerIndex());
        System.out.println(byteBuf.writerIndex());
    }
}
