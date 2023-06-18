package io_model;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class SocketMultiplexingSingleThreadV1 {

    private ServerSocketChannel server = null;
    private Selector selector = null;   // 用的是 epoll

    public void initServer() {
        try {
            server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(9090));
            server.configureBlocking(false);

            selector = Selector.open();
            System.out.println("@@@@ epoll_create() @@@@");

            server.register(selector, SelectionKey.OP_ACCEPT);  // 这里并没有真正调用 epoll_ctl
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        initServer();
        System.out.println("-----------------服务已启动-----------------");
        try {
            while (true) {
                int selectCount = selector.select();  // 这里的返回值大小和 keys 的数量相等，值代表了 server 和 client 的数量之和
                System.out.println("@@@@ epoll_ctl()  @@@@");
                System.out.println("@@@@ epoll_wait() @@@@");

                if (selectCount > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();  // 这里返回的是可以进行处理的 server 或 clients。这里拿到的都是有状态的 server 或 clients
                    Iterator<SelectionKey> iter = keys.iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();   // 这里必须 remove。keys 是复用同一批对象的，对其内部的元素移除之后，程序可以判断事件是否处理了

                        // 只关心两种状态 acceptable 和 readable
                        if (key.isAcceptable()) {
                            acceptHandler(key);  // 处理 server 逻辑，因为有新的连接进来了
                        } else if (key.isReadable()) {
                            readHandler(key);    // 处理 client 逻辑，因为有数据进来了
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SocketMultiplexingSingleThreadV1().start();
    }

    public void acceptHandler(SelectionKey serverKey) {
        try {
            ServerSocketChannel server = (ServerSocketChannel)serverKey.channel();
            SocketChannel client = server.accept();   //这个事件必须处理
            client.configureBlocking(false);
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);  // 可以理解成一个回环数组。一个连接(client)对应一个byteBuffer
//            ByteBuffer.allocate(1024);  堆内 bytebuffer
//            ByteBuffer.allocateDirect(1024);  堆外 bytebuffer
            client.register(selector, SelectionKey.OP_READ, byteBuffer);      // 这里并没有真正调用 epoll_ctl
            System.out.println("---------------- server 事件（连接建立事件）已处理：" + client.getRemoteAddress() + "-----------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readHandler(SelectionKey clientKey) throws IOException {
        // 准备工作
        SocketChannel client = (SocketChannel) clientKey.channel();
        ByteBuffer byteBuffer = (ByteBuffer) clientKey.attachment();
        byteBuffer.clear();  // 重置 ByteBuffer(不会实际删除数据)

        try {
            while (true) {
                int read = client.read(byteBuffer);  // client 从 byteBuffer 的 position 位置开始写,总共写 limit 个字节的数据
                if (read > 0) {
                    byteBuffer.flip();  // 回环,读取 byteBuffer 中的数据前执行 flip
                    byte[] mybytes = new byte[byteBuffer.limit()];
                    byteBuffer.get(mybytes);
                    System.out.println("---------------- server 读取数据：" + new String(mybytes) + "-----------------------");

                    while (byteBuffer.hasRemaining()) {
                        client.write(byteBuffer);
                    }
                    byteBuffer.clear();
                } else if (read == 0) {
                    break;
                } else {
                    client.close();  // 客户端断开连接之后,会收到一个 readable 的事件(实质上是因为连接状态变成了 close_wait),read 出来的值是 -1(只有 linux 这样)
                    System.out.println("---------------- 客户端断开连接 -----------------------");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // client.close(); 当客户端断开连接时,Windows 会在 read 的时候直接抛出 io 异常
        }
    }
}