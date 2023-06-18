package io_model;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class BossGroupRunnable implements Runnable {

    private final Selector selector = Selector.open();
    private final WorkerGroupRunnable workerGroupRunnable1 = new WorkerGroupRunnable();
    private final WorkerGroupRunnable workerGroupRunnable2 = new WorkerGroupRunnable();

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
                                workerGroupRunnable1.register(worker);
                                System.out.println(worker.getRemoteAddress() + " worker 被注册到线程1");
                            } else {
                                workerGroupRunnable2.register(worker);
                                System.out.println(worker.getRemoteAddress() + " worker 被注册到线程2");
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
