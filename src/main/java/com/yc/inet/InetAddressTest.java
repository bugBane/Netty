package com.yc.inet;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressTest {
    public static void main(String[] args) {
        try {
//            InetAddress inetAddress1 = InetAddress.getByName("127.0.0.1");
//            InetAddress inetAddress1 = InetAddress.getByName("localhost");
            InetAddress inetAddress1 = InetAddress.getLocalHost();
            System.out.println(inetAddress1);

            InetAddress inetAddress2 = InetAddress.getByName("www.baidu.com");
            System.out.println(inetAddress2);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
