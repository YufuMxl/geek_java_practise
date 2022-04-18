package fourth_week;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantDemo {

    private static final Object lock1 = new Object();

    public static void main(String[] args) {
        Runnable task = () -> {
            synchronized (lock1) {
                System.out.println("当前线程对 lock1 加了第一层锁");
                synchronized (lock1) {
                    System.out.println("当前线程对 lock1 加了第二层锁");
                    synchronized (lock1) {
                        System.out.println("当前线程对 lock1 加了第三层锁");
                    }
                }
            }
        };
        new Thread(task).start();

        final Lock lock = new ReentrantLock();
        Runnable task2 = () -> {
            try {
                lock.lock();
                System.out.println("当前线程对 lock1 加了第一层锁");

                lock.lock();
                System.out.println("当前线程对 lock1 加了第二层锁");

                lock.lock();
                System.out.println("当前线程对 lock1 加了第三层锁");
            } finally {
                // 加多少次锁，解多少次锁
                lock.unlock();
                lock.unlock();
                lock.unlock();
            }
        };
        new Thread(task2).start();
        new Thread(task2).start();
    }
}
