package fourth_week;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Atomic 变量自增运算测试
 */
public class AtomicTest {

    public static AtomicInteger race = new AtomicInteger(0);

    public static void increase() {
        race.incrementAndGet();
    }

    private static final int THREADS_COUNT = 20;

    public static void main(String[] args) throws Exception {
        for (Thread myThread : new Thread[THREADS_COUNT]) {
            myThread = new Thread(() -> {
                for (int i = 0; i < 10000; i++) {
                    increase();
                }
            });
            myThread.start();
        }

        Thread.sleep(2000);
        System.out.println(race);
    }

}


