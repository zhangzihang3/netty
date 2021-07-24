package com.zzh.netty.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 磁盘->内存 FileInputStream
 */
public class demo2 {
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\zzhwork\\netty\\src\\main\\resources\\static\\学习笔记.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());
        //将文件中的数据存入byteBuffer
        channel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();

    }
}
