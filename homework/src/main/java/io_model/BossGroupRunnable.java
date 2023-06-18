package io_model;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class BossGroupRunnable implements Runnable {

    private final Selector selector = Selector.open();
    private final WorkerGroupRunnable workerGroupRunnable1 = new WorkerGroupRunnable(worker1Queue);
    private final WorkerGroupRunnable workerGroupRunnable2 = new WorkerGroupRunnable(worker2Queue);
    private final static Queue<SocketChannel> worker1Queue = new ArrayDeque<>();
    private final static Queue<SocketChannel> worker2Queue = new ArrayDeque<>();

    public BossGroupRunnable(ServerSocketChannel boss) throws IOException {
        boss.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("boss 注册进 boss group");
        new Thread(workerGroupRunnable1).start();
        new Thread(workerGroupRunnable2).start();
    }

    @Override
    public void run() {
        System.out.println("Boss 线程开始执行：" + Thread.currentThread().getId());
        try {
            int count = 0;
            while (true) {
                int selectCount = selector.select();
                if (selectCount > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey next = iterator.next();
                        iterator.remove();

                        if (next.isAcceptable()) {
                            ServerSocketChannel boss = (ServerSocketChannel)next.channel();
                            SocketChannel worker = boss.accept();
                            worker.configureBlocking(false);

                            if (count % 2 == 0) {
                                worker1Queue.offer(worker);
                            } else {
                                worker2Queue.offer(worker);
                            }
                        }
                    }
                }
                count ++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
