package fourth_week;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void writeLockDemo() {
        String threadName = Thread.currentThread().getName();
        readWriteLock.writeLock().lock();
        try {
            if (threadName.equals("thread 1")) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
            System.out.println("Element by thread " + threadName + " is written");
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public void readLockDemo() {
        String threadName = Thread.currentThread().getName();
        readWriteLock.readLock().lock();
        try {
            if (threadName.equals("thread 3")) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
            System.out.println("Elements by thread " + threadName + " is read");
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public static void main(String[] args) {
        ReadWriteLockDemo readWriteLockDemo = new ReadWriteLockDemo();

        // 写锁被独占（thread 1 和 thread 2 按顺序打印）
        System.out.println("thread 1 started");
        new Thread(readWriteLockDemo::writeLockDemo, "thread 1").start();
        System.out.println("thread 2 started");
        new Thread(readWriteLockDemo::writeLockDemo, "thread 2").start();

        // 读锁被公用（thread 4 优先于 thread 3 打印）
        // 读锁与写锁互斥（thread 3/4 等待 thread 1 执行完再打印）
        System.out.println("thread 3 started");
        new Thread(readWriteLockDemo::readLockDemo, "thread 3").start();
        System.out.println("thread 4 started");
        new Thread(readWriteLockDemo::readLockDemo, "thread 4").start();

        // 写锁与读锁互斥（thread 5 等待 thread 3 执行完再打印）
        System.out.println("thread 5 started");
        new Thread(readWriteLockDemo::writeLockDemo, "thread 5").start();
    }
}