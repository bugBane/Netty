package com.yc.netty.protocoltcp;

// 协议包
public class MessageProtocol {
    // 长度
    private int length;
    // 数据
    private byte[] data;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
