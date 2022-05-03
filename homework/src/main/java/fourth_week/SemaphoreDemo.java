package fourth_week;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

    public static void main(String[] args) {
        final int workerNum = 8;
        final Semaphore semaphore = new Semaphore(2);

        for (int i = 0; i < workerNum; i++) {
            new Worker(i, semaphore).start();
        }
    }

    static class Worker extends Thread {
        final int num;
        final Semaphore semaphore;

        Worker(int num, Semaphore semaphore) {
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println("工人" + this.num + "在占用一个机器");
                Thread.sleep(2000);
                System.out.println("工人" + this.num + "释放了一个机器");
                semaphore.release();
            } catch (InterruptedException ignored) {
            }
        }
    }
}
