package com.zzh.netty.groupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient2 {
    private SocketChannel socketChannel;
    private Selector selector;
    private String userName;
    private static final int port = 2222;

    public GroupChatClient2() {
        try {
            selector = Selector.open();
            socketChannel = socketChannel.open(new InetSocketAddress("127.0.0.1", port));
            socketChannel.configureBlocking(false);
            //注册客户端是可读的
            socketChannel.register(selector, SelectionKey.OP_READ);
            userName = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(userName + " is ok......");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(String info) {
        info = userName + " 说：" + info;
        try {
            //将消息写入客户端通道里面去
            socketChannel.write(ByteBuffer.wrap(info.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readInfo() {
        try {
            int readCount = selector.select();
            if (readCount > 0) {
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer data = ByteBuffer.allocate(1024);
                        readCount = client.read(data);
                        if (readCount > 0) {
                            System.out.println("读取到服务端发来的消息：" + new String(data.array()));
                        }
                    }
                }
                keys.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws Exception {

        //启动我们客户端
        GroupChatClient2 chatClient = new GroupChatClient2();

        //启动一个线程, 每个3秒，读取从服务器发送数据
        new Thread() {
            public void run() {
                while (true) {
                    chatClient.readInfo();
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        //发送数据给服务器端
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }

}
