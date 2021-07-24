package com.zzh.netty.channel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * byteBuffer的分散与聚集
 */
public class demo7 {
    public static void main(String[] args) throws IOException {
        //开启一个serverSocket通道
        ServerSocketChannel server = ServerSocketChannel.open();
        //通道绑定端口号
        server.bind(new InetSocketAddress(6000));
        //初始化byteBuffer数组
        ByteBuffer[] bufferBytes = new ByteBuffer[3];
        bufferBytes[0] = ByteBuffer.allocate(5);
        bufferBytes[1] = ByteBuffer.allocate(5);
        bufferBytes[2] = ByteBuffer.allocate(5);
        //等待接受连接
        SocketChannel client = server.accept();
        while (true) {
            int readCursor = 0;
            while (readCursor < 8) {
                long read = client.read(bufferBytes);
                readCursor += read;
                System.out.println("累计读取到的字节数：" + readCursor);
                //打印每个BufferByte中的数据情况
                Arrays.asList(bufferBytes)
                        .stream()
                        .map(buffer -> "position:" + buffer.position() + ",limit:" + buffer.limit()+"\n").forEach(System.out::print);
            }
            //对每个buffer都进行反转
            Arrays.asList(bufferBytes).forEach(buffer -> buffer.flip());
            int writeCursor = 0;
            while (writeCursor < 8) {
                long write = client.write(bufferBytes);
                writeCursor+=write;
            }
            Arrays.asList(bufferBytes).forEach(buffer -> buffer.clear());
            System.out.println("总共读取的字节数："+readCursor+"总共写入的字节数："+writeCursor);
        }


    }
}
