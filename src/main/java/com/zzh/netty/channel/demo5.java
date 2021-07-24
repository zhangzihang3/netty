package com.zzh.netty.channel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * transferFrom拷贝
 */
public class demo5 {
    public static void main(String[] args) throws IOException {
        //磁盘->内存
        FileInputStream fileInputStream = new FileInputStream("D:\\zzhwork\\netty\\src\\main\\resources\\static\\a.jpg");
        FileChannel targetChannel = fileInputStream.getChannel();
        //内存->磁盘
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\zzhwork\\netty\\src\\main\\resources\\static\\b.jpg");
        FileChannel proxyChannel = fileOutputStream.getChannel();

        //将targetChannel中的数据复制到proxyChannel通道中
        proxyChannel.transferFrom(targetChannel,0,targetChannel.size());

        targetChannel.close();
        proxyChannel.close();

        fileInputStream.close();
        fileOutputStream.close();
    }
}
