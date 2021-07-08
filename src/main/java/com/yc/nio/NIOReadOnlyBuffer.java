package com.yc.nio;

import java.nio.ByteBuffer;

/**
 * 只读Buffer
 */
public class NIOReadOnlyBuffer {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        for (int i = 0; i < 10; i++) {
            byteBuffer.putInt(i);
        }
        // 读取就反转
        byteBuffer.flip();
        // 得到一个只读Buffer
        ByteBuffer asReadOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        while (asReadOnlyBuffer.hasRemaining()) {
            System.out.println(asReadOnlyBuffer.getInt());
        }
        // 只读Buffer不能put数据，否则抛异常 ReadOnlyBufferException
//        asReadOnlyBuffer.putInt(11);
        System.out.println(asReadOnlyBuffer.getClass());
    }
}
