package com.yc.url;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 根据url下载资源
 */
public class UrlDownDemo {
    public static void main(String[] args) throws Exception {
        // 下载地址
        URL url = new URL("https://dss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/weather/icons/a1.png");
        // 打开http连接
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        // 获取资源
        InputStream is = httpURLConnection.getInputStream();
        // 下载到本地
        FileOutputStream fos = new FileOutputStream(new File("D:\\ycPro\\src\\main\\resources\\a2.png"));
        byte[] bytes = new byte[1024];
        int len;
        while ((len = is.read(bytes)) != -1) {
            fos.write(bytes, 0, len);
        }
        // 关闭资源
        fos.close();
        is.close();
    }
}
