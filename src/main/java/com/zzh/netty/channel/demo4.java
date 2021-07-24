package com.zzh.netty.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * byteBuffer （put、get顺序问题）
 * 顺序存入、顺序取出
 */
public class demo4 {
    public static void main(String[] args) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);

        byteBuffer.putChar('b');
        byteBuffer.putDouble(2D);
        byteBuffer.putFloat(2F);
        byteBuffer.putInt(2);

        byteBuffer.flip();

        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getDouble());
        System.out.println(byteBuffer.getFloat());
        System.out.println(byteBuffer.getInt());
    }
}
