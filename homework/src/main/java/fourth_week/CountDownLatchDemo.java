package fourth_week;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + "线程运行");
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + "线程 countDown");
                countDownLatch.countDown();
            } catch (InterruptedException ignored) {
            }
        };

        new Thread(runnable).start();
        new Thread(runnable).start();
        System.out.println("主线程阻塞");
        countDownLatch.await();
        System.out.println("主线程继续运行");
    }
}
