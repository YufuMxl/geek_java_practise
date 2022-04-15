package third_week.threadpool;

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
        private AtomicInteger serial = new AtomicInteger(0);

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

    // 验证线程池是否一开始就创建线程
    public static void main(String[] args) throws InterruptedException {
        ExecutorService threadPool = newThreadPoolDemo();
        Thread.sleep(15000);
        threadPool.execute(() -> System.out.println("创建了一个线程"));
        Thread.sleep(15000);
        threadPool.execute(() -> System.out.println("创建了另一个线程"));

        for (int i = 0; i < 10; i++) {
            Thread.sleep(15000);
        }
    }

}
