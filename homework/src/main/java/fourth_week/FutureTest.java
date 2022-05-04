package fourth_week;

import third_week.threadpool.ThreadPoolDemo;

import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class FutureTest {

    public static void main(String[] args) throws Exception {
        final ExecutorService threadPool = ThreadPoolDemo.newThreadPoolExecutor();
        futureDemo(threadPool);
        futureTaskDemo(threadPool);
        completableFutureDemo1();
        completableFutureDemo2();
        completableFutureDemo3();
        completableFutureDemo4();
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
            } catch (InterruptedException ignored) {
            }
            return "completed within thread";
        });

        Thread.sleep(200L); // 移除 sleep，supplyAsync 内部的任务就不再执行
        future.complete("completed outside thread");
        System.out.println(future.get());

        Thread.sleep(2000L);
        // 虽然 supplyAsync 内部的任务正常执行结束，但是 get 方法的返回值还是 complete 方法传入的值
        System.out.println("wait for sout When finished, call again get: " + future.get());
    }

    /**
     * CompletableFuture 可以添加回调方法
     */
    public static void completableFutureDemo2() throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is running");
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ignored) {
            }
            return "completed within thread";
        }).thenApply((string) -> {
            System.out.println(Thread.currentThread().getName() + " is calling callback");
            return string + " with callback";
        });
        System.out.println(future.get());
    }

    /**
     * CompletableFuture 可以组合多个任务以获得结果
     */
    public static void completableFutureDemo3() throws InterruptedException, ExecutionException {
        Supplier<String> Supplier = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("线程 " + threadName + " 开始执行任务");
            try {
                Thread.sleep(new Random().nextInt(10000));
            } catch (InterruptedException ignored) {
            }
            System.out.println("线程 " + threadName + " 执行任务结束");
            return threadName;
        };

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(Supplier);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(Supplier);
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(Supplier);
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(future1, future2, future3);
        System.out.println(completableFuture.get() + " 所有任务执行结束");

        CompletableFuture<String> future4 = CompletableFuture.supplyAsync(Supplier);
        CompletableFuture<String> future5 = CompletableFuture.supplyAsync(Supplier);
        CompletableFuture<String> future6 = CompletableFuture.supplyAsync(Supplier);
        CompletableFuture<Object> objectCompletableFuture = CompletableFuture.anyOf(future4, future5, future6);
        System.out.println("获取最先执行结束的线程返回值： " + objectCompletableFuture.get());
        Thread.sleep(10000);
    }

    /**
     * CompletableFuture 可以处理异常
     */
    public static void completableFutureDemo4() throws InterruptedException, ExecutionException {
        CompletableFuture<String> handle = CompletableFuture
            .runAsync(() -> System.out.println("线程 " + Thread.currentThread().getName() + " 开始执行任务"))
            .handle((unused, throwable) -> {
                if (null == throwable) return "ok";
                else return "error";
            });
        System.out.println(handle.get());

        CompletableFuture<String> exceptionally = CompletableFuture
            .supplyAsync(() -> String.valueOf(1 / 0))
            .exceptionally(throwable -> "error");
        System.out.println(exceptionally.get());
    }
}
