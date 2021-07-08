package com.yc.nio;

import java.nio.ByteBuffer;

/**
 * 缓存区Buffer的put和get
 */
public class NIOByteBufferPutGet {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.putInt(1);
        byteBuffer.putChar('2');
        byteBuffer.putShort((short) 3);
        byteBuffer.putLong(4);

        byteBuffer.flip();

        // 注意：查询的顺序与插入的循序不一致，会导致报错(flip反转只是改变了索引，取值类似于迭代器)
        System.out.println(byteBuffer.getInt());
        // 这个报错
        System.out.println(byteBuffer.getShort());
        // 这个显示有问题
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getLong());
    }
}
