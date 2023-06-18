package io_model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class WorkerGroupRunnable implements Runnable {

    private final Selector selector = Selector.open();

    public WorkerGroupRunnable() throws IOException {
    }

    public void register(SocketChannel worker) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        worker.register(selector, SelectionKey.OP_READ, byteBuffer);
    }

    @Override
    public void run() {
        System.out.println("worker 线程开始执行：" + Thread.currentThread().getId());
        try {
            while (true) {
                int selectCount = selector.select(200);  // 为避免线程一启动，就阻塞在这，这里必须设置 timeout
                if (selectCount > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey next = iterator.next();
                        iterator.remove();

                        if (next.isReadable()) {
                            readHandler(next);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readHandler(SelectionKey selectionKey) throws IOException {
        // 准备工作
        SocketChannel worker = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
        byteBuffer.clear();  // 重置 ByteBuffer(不会实际删除数据)

        try {
            while (true) {
                int read = worker.read(byteBuffer);
                if (read > 0) {
                    byteBuffer.flip();
                    byte[] mybytes = new byte[byteBuffer.limit()];
                    byteBuffer.get(mybytes);
                    System.out.println(Thread.currentThread().getId()+"---------------- 读取数据：" + new String(mybytes) + "-----------------------");

                    while (byteBuffer.hasRemaining()) {
                        worker.write(byteBuffer);
                    }
                    byteBuffer.clear();
                } else if (read == 0) {
                    break;
                } else {
                    worker.close();
                    System.out.println("---------------- 客户端断开连接 -----------------------" + Thread.currentThread().getId());
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("---------------- 客户端断开连接 -----------------------" + Thread.currentThread().getId());
            worker.close();
        }
    }
}
