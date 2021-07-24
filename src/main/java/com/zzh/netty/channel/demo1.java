package com.zzh.netty.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 内存->磁盘 FileOutputStream
 */
public class demo1 {
    public static void main(String[] args) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        String hello = "hello";
        File file = new File("D:\\zzhwork\\netty\\src\\main\\resources\\static\\学习笔记.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        FileChannel channel = fileOutputStream.getChannel();
        byteBuffer.put(hello.getBytes());
        //切换读模式
        byteBuffer.flip();
        //将buffer中的数据写入通道
        channel.write(byteBuffer);
        fileOutputStream.close();

    }
}
