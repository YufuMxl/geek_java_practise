package fourth_week;

import third_week.threadpool.ThreadPoolDemo;

import java.util.concurrent.*;

public class FutureTest {

    public static void main(String[] args) throws Exception {
        final ExecutorService threadPool = ThreadPoolDemo.newThreadPoolExecutor();
        futureDemo(threadPool);
        futureTaskDemo(threadPool);
        completableFutureDemo1();
    }

    public static void futureDemo(ExecutorService threadPool) throws ExecutionException, InterruptedException {
        Future<String> submit = threadPool.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("线程 " + threadName + " 正在运行中");
            return threadName;
        });
        System.out.println("线程 " + submit.get() + " 已返回");
    }

    public static void futureTaskDemo(ExecutorService threadPool) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("线程 " + threadName + " 正在运行中");
            return threadName;
        });
        threadPool.execute(futureTask);
        System.out.println("线程 " + futureTask.get() + " 已返回");

        FutureTask<String> futureTask2 = new FutureTask<>(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("线程 " + threadName + " 正在运行中");
        }, "default value");
        threadPool.execute(futureTask2);
        System.out.println("线程 " + futureTask2.get() + " 已返回");
    }

    /**
     * CompletableFuture 可以手动完成
     */
    public static void completableFutureDemo1() throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000L);
                System.out.println("The asynchronous running method was run" + Thread.currentThread().getName());
                return "completed within thread";
            } catch (InterruptedException e) {
                return e.getMessage();
            }
        });

        Thread.sleep(200L); // 移除 sleep，supplyAsync 内部的任务就不再执行
        future.complete("completed outside thread");
        System.out.println(future.get());

        Thread.sleep(2000L);
        // 虽然 supplyAsync 内部的任务正常执行结束，但是 get 方法的返回值还是 complete 方法传入的值
        System.out.println("wait for sout When finished, call again get: " + future.get());
    }

}
