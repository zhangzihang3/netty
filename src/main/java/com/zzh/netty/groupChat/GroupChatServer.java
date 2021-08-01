package com.zzh.netty.groupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static final int port = 2222;
    public GroupChatServer() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            //注册selector、OP_ACCEPT(有新的连接)
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            while (true) {
                int count = selector.select(2000);
                if (count > 0) {
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                    while (keys.hasNext()) {
                        SelectionKey key = keys.next();
                        if (key.isAcceptable()) {
                            SocketChannel clientChannel = serverSocketChannel.accept();
                            clientChannel.configureBlocking(false);
                            //将此客户端注册到此selector
                            clientChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(clientChannel.getRemoteAddress() + "上线了。");
                        }
                        //通道是可读的状态
                        if (key.isReadable()) {
                            readData(key);
                        }
                        keys.remove();
                    }
                } else {
                   // System.out.println("等待");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("发生异常处理");
        }
    }

    private void readData(SelectionKey key) {
        SocketChannel client = null;
        try {
            client = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = client.read(byteBuffer);
            //读取到了数据
            if (count > 0) {
                String msg = new String(byteBuffer.array());
                System.out.println("收到客户端的消息：" + msg);
                //转发消息
                sendInfoToOtherClient(msg, client);
            }
        } catch (Exception e) {
            try {
                System.out.println(client.getRemoteAddress() + "离线了");
                //取消注册
                key.cancel();
                //关闭通道
                client.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }
    }

    private void sendInfoToOtherClient(String msg, SocketChannel self) {
        try {
            System.out.println("服务器转发消息中。。。。。");
            for (SelectionKey key : selector.selectedKeys()) {
                Channel otherClient = key.channel();
                //排除转发消息给自己这种情况
                if (otherClient instanceof SocketChannel && otherClient != self) {
                    ByteBuffer data = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
                    //将消息转发给其他客户端
                    ((SocketChannel) otherClient).write(data);
                    System.out.println("转发成功");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //创建服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }

}
