package io_model;

// 在 netty 中，有 boss 和 worker 的概念，对应 ServerSocketChannel 和 SocketChannel
// 只注册了 ServerSocketChannel 的 selector 称为 boss group，对应的线程称为 boss 线程。该线程专门处理 accept 工作
// 只注册了 SocketChannel 的 selector 称为 worker group，对应的线程称为 worker 线程。处理 recv、write、以及计算工作（计算工作是不是可以交给别的线程？？？）
// 每一个线程对应一个 selector，每个线程只负责自己 selector 的 select & get keys 工作，keys 在线程之间不互串

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class SocketMultiplexingThreads {

    private ServerSocketChannel boss = null;

    private void start() {
        try {
            this.boss = ServerSocketChannel.open();
            this.boss.bind(new InetSocketAddress(9090));
            this.boss.configureBlocking(false);
            System.out.println("服务开始监听");

            BossGroupRunnable bossGroupRunnable = new BossGroupRunnable(boss);  // 这是一个包含了 boss group 的任务执行器
            new Thread(bossGroupRunnable).start();  // 这是一个 boss 线程
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SocketMultiplexingThreads().start();
    }
}
