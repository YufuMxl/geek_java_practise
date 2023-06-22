package io_model;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyIO {

    public static void main(String[] args) {
        // 多个 NioEventLoop 的 Group，每个 NioEventLoop 包含一个单线程的线程池，以及一个 Selector
        // 可以认为，构造函数中的 nThreads 即是线程数，又是 NioEventLoop 的数量
        // NioEventLoopGroup bossGroup = new NioEventLoopGroup(1); // worker（SocketChannel）和 boss（ServerSocketChannel）共用一个线程
        // NioEventLoopGroup bossGroup = new NioEventLoopGroup(4); // 混合模式，某个线程即有 boss，又有 worker；其余线程只有 worker
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(3);
        ServerBootstrap boot = new ServerBootstrap();
        try {
            // boot.group(bossGroup, bossGroup)
            boot.group(bossGroup, workerGroup) // 主从模式，boss 在单独的线程内。此时一共有 3 个而不是 4 个线程(线程按需创建，ServerSocketChannel 只有一个，对应的线程就只有一个)
                    .channel(NioServerSocketChannel.class) // 创建 ServerSocketChannel
                    .childHandler(new ChannelInitializer<NioSocketChannel>() { // 创建 SocketChannel
                        @Override
                        protected void initChannel(NioSocketChannel ch) {
                            ch.pipeline().addLast(new MyInbound());
                        }
                    });

            // IO 永远是同步的，处理/计算（Future 干的事）是异步的
            ChannelFuture future  = boot.bind(9090).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class MyInbound extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ctx.write(msg); // 从客户端 read 到的东西写回客户端
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }
    }
}

// epoll 通过事件通知机制，减少轮询（轮询文件描述符集合）开销
// fd 绑定相关的事件类型（accept、read、write），经 epoll_ctl(fd) 注册到内核的事件表（记事本）中
// 应用程序调用 epoll_wait() 等待事件（阻塞）；当 fd 上的事件发生时，内核会将该 fd 添加到就绪队列并通知应用程序


// mainReactor 是一个 selector
// subReactor 每一个 worker 是一个 selector