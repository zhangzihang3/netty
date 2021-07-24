package com.zzh.netty.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * channel.map(FileChannel.MapMode.READ_WRITE, 0, 3);
 * 将FileChannel中的3个字节映射到MappedByteBuffer中.
 * 查看效果要在文件中去点开
 */
public class demo6 {
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\zzhwork\\netty\\src\\main\\resources\\static\\RandomAccessFile.txt");
        //可读写文件
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileChannel channel = randomAccessFile.getChannel();
        //得到的镜像可读可写
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 3);
        //将第0号位置的字节修改为1
        map.put(0, (byte) '1');
        map.put(1, (byte) '2');
        map.put(2, (byte) '3');
        //map.put(3, (byte) '3'); IndexOutOfBoundsException
        randomAccessFile.close();
    }
}
