package com.zzh.netty.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件拷贝 通道->byteBuffer->通道
 */
public class demo3 {
    public static void main(String[] args) throws IOException {
//磁盘->内存
        FileInputStream fileInputStream = new FileInputStream("D:\\zzhwork\\netty\\src\\main\\resources\\static\\target.txt");
        FileChannel targetChannel = fileInputStream.getChannel();
//内存->磁盘
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\zzhwork\\netty\\src\\main\\resources\\static\\proxy.txt");
        FileChannel proxyChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(100000000);
        while (true) {
            //将byteBuffer标识位清除
            byteBuffer.clear();
            //将targetChannel中的数据读取出来，写入byteBuffer
            int read = targetChannel.read(byteBuffer);
            if (read == -1) {
                break;
            }
            byteBuffer.flip();
            //将byteBuffer中的数据读取出来，写入proxy通道
            proxyChannel.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }
}
