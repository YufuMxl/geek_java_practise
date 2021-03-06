package third_week.threadpool;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolDemo {

    // 顶级接口
    private Executor executor;
    // 线程池 API，extends Executor
    private ExecutorService executorService;
    // 线程池实现，extends AbstractExecutorService implements ExecutorService
    private ThreadPoolExecutor threadPoolExecutor;

    // 自定义线程工厂
    static class CustomThreadFactory implements ThreadFactory {
        private final AtomicInteger serial = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            // 根据需要，设置守护线程
            thread.setDaemon(true);
            thread.setName("CustomThread-" + serial.getAndIncrement());
            return thread;
        }
    }

    // Executors 工具用于创建各种线程池
    public static ExecutorService newThreadPoolDemo() {
        ThreadFactory threadFactory = new CustomThreadFactory();
        // 单线程的线程池
        ExecutorService singleThreadExecutor1 = Executors.newSingleThreadExecutor();
        ExecutorService singleThreadExecutor2 = Executors.newSingleThreadExecutor(threadFactory);

        // 固定大小的线程池
        ExecutorService fixedThreadPool1 = Executors.newFixedThreadPool(10);
        ExecutorService fixedThreadPool2 = Executors.newFixedThreadPool(10, threadFactory);

        // 可缓存的线程池
        ExecutorService cachedThreadPool1 = Executors.newCachedThreadPool();
        ExecutorService cachedThreadPool2 = Executors.newCachedThreadPool(threadFactory);

        // 大小无限的线程池
        ExecutorService scheduledThreadPool1 = Executors.newScheduledThreadPool(10);
        ExecutorService scheduledThreadPool2 = Executors.newScheduledThreadPool(10, threadFactory);

        return fixedThreadPool2;
    }

    public static ExecutorService newThreadPoolExecutor() {
        // availableProcessors 为 CPU 物理线程数
        // 由于 Intel 处理器有超线程技术，物理线程数 = CPU 核数 * 2
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        int maximumPoolSize = corePoolSize * 2;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(500);
        ThreadFactory threadFactory = new CustomThreadFactory();
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 1, TimeUnit.MINUTES, workQueue, threadFactory);
    }

    // 验证线程池是否一开始就创建线程
    public static void main(String[] args) throws Exception {
        ExecutorService threadPool = newThreadPoolDemo();
        Thread.sleep(15000);
        threadPool.execute(() -> System.out.println("创建了一个线程"));
        Thread.sleep(15000);
        threadPool.execute(() -> System.out.println("创建了另一个线程"));

        futureDemo();

        for (int i = 0; i < 10; i++) {
            Thread.sleep(15000);
        }
    }

    // Callable 接口示例
    static class RandomSleepTask implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            int sleep = new Random().nextInt(10000);
            TimeUnit.MILLISECONDS.sleep(sleep);
            return sleep;
        }
    }

    // Future 接口示例
    public static void futureDemo() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService threadPool = newThreadPoolExecutor();
        Future<Integer> future1 = threadPool.submit(new RandomSleepTask());
        Future<Integer> future2 = threadPool.submit(new RandomSleepTask());
        // 等待执行结果
        Integer result1 = future1.get(2000, TimeUnit.MILLISECONDS);
        System.out.println("result1=" + result1);
        Integer result2 = future2.get(2000, TimeUnit.MILLISECONDS);
        System.out.println("result2=" + result2);
    }

}
