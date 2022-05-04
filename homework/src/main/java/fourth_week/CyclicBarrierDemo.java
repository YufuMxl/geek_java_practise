package fourth_week;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CyclicBarrierDemo {

    public static void main(String[] args) throws InterruptedException {
        final int threadNum = 5;
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(
            threadNum,
            () -> System.out.println(threadNum + " 个线程到齐，由线程 " + Thread.currentThread().getName() + " 触发执行")
        );

        final Runnable runnable = () -> {
            String threadName = Thread.currentThread().getName();
            try {
                System.out.println(threadName + "线程等待其他线程");
                cyclicBarrier.await(6, TimeUnit.SECONDS);
                System.out.println(threadName + "线程继续运行");
            } catch (InterruptedException | BrokenBarrierException e) {
                System.out.println(threadName + " 出现 BrokenBarrier 异常，由 cyclicBarrier.reset() 导致");
            } catch (TimeoutException e) {
                System.out.println(threadName + " 出现 TimeoutException 异常，由 await() 超时导致");
            }
        };

        for (int i = 0; i < 99; i++) {
            System.out.println("等待的线程数为：" + cyclicBarrier.getNumberWaiting());
            new Thread(runnable).start();
            Thread.sleep(1000);
        }
    }
}
