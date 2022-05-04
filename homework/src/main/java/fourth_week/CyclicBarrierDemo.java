package fourth_week;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {

    public static void main(String[] args) throws InterruptedException {
        final int threadNum = 5;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNum, () -> System.out.println(threadNum + " 个线程到齐"));

        final Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + "线程等待其他线程");
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + "线程继续运行");
            } catch (InterruptedException | BrokenBarrierException ignored) {
            }
        };

        for (int i = 0; i < threadNum; i++) {
            new Thread(runnable).start();
            Thread.sleep(1000);
        }
    }
}
