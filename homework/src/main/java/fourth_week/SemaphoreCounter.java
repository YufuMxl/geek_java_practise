package fourth_week;

import java.util.concurrent.Semaphore;

public class SemaphoreCounter {
    private int sum = 0;
    private final Semaphore semaphore = new Semaphore(2);

    public void incrementAndGet() {
        String threadName = Thread.currentThread().getName();
        try {
            semaphore.acquireUninterruptibly();
            if (threadName.equals("Thread-0")) Thread.sleep(1000);
            System.out.println(threadName + " " + ++sum);
        } catch (InterruptedException ignored) {
        } finally {
            semaphore.release();
        }
    }

    public static void main(String[] args) {
        SemaphoreCounter counter = new SemaphoreCounter();
        Thread thread1 = new Thread(counter::incrementAndGet);
        Thread thread2 = new Thread(counter::incrementAndGet);
        thread1.start();
        thread2.start();
    }
}
