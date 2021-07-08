package com.yc.inet;

import java.net.InetSocketAddress;

public class InetSocketAddressTest {
    public static void main(String[] args){
        InetSocketAddress inetSocketAddress1 = new InetSocketAddress("127.0.0.1", 8080);
        InetSocketAddress inetSocketAddress2 = new InetSocketAddress("localhost", 8080);

        System.out.println(inetSocketAddress1);
        System.out.println(inetSocketAddress2);

        System.out.println(inetSocketAddress1.getAddress());
        System.out.println(inetSocketAddress1.getHostName());
        System.out.println(inetSocketAddress1.getPort());
    }
}
