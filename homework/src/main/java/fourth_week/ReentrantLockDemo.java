package fourth_week;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    private static final ReentrantLockDemo reentrantLockDemo = new ReentrantLockDemo();

    public static void main(String[] args) throws Exception {
        // 1.lock() 演示
        Lock lockLock = new ReentrantLock();
        new Thread(() -> reentrantLockDemo.lockDemo(lockLock), "lockDemo1").start();
        new Thread(() -> reentrantLockDemo.lockDemo(lockLock), "lockDemo2").start();

        // 2.lockInterruptibly() 演示
        Lock lockInterruptiblyLock = new ReentrantLock();
        new Thread(
            () -> reentrantLockDemo.lockInterruptiblyDemo(lockInterruptiblyLock), "lockInterruptiblyDemo1"
        ).start();
        Thread lockInterruptiblyDemo2 = new Thread(
            () -> reentrantLockDemo.lockInterruptiblyDemo(lockInterruptiblyLock), "lockInterruptiblyDemo2"
        );
        lockInterruptiblyDemo2.start();
        lockInterruptiblyDemo2.interrupt();

        // 3.tryLock() 演示
        Lock tryLockLock = new ReentrantLock();
        new Thread(() -> reentrantLockDemo.tryLockDemo(tryLockLock), "tryLockDemo1").start();
        new Thread(() -> reentrantLockDemo.tryLockDemo(tryLockLock), "tryLockDemo2").start();

        // 4.超时 tryLock() 演示
        Lock tryLockWithTimeOutLock = new ReentrantLock();
        new Thread(
            () -> reentrantLockDemo.tryLockWithTimeOutDemo(tryLockWithTimeOutLock), "tryLockWithTimeOutDemo1"
        ).start();
        Thread tryLockWithTimeOutDemo2 = new Thread(
            () -> reentrantLockDemo.tryLockWithTimeOutDemo(tryLockWithTimeOutLock), "tryLockWithTimeOutDemo2"
        );
        tryLockWithTimeOutDemo2.start();
        Thread.sleep(1000);
        tryLockWithTimeOutDemo2.interrupt();

    }

    public void lockDemo(Lock lock) {
        lock.lock();
        try {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName());
        } catch (InterruptedException ignored) {
        } finally {
            lock.unlock();
        }
    }

    public void lockInterruptiblyDemo(Lock lock) {
        String threadName = Thread.currentThread().getName();
        try {
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            System.out.println(threadName + " 线程被中断");
            return;
        }

        try {
            Thread.sleep(1000);
            System.out.println(threadName);
        } catch (InterruptedException ignored) {
        } finally {
            lock.unlock();
        }
    }

    public void tryLockDemo(Lock lock) {
        String threadName = Thread.currentThread().getName();
        if (lock.tryLock()) {
            try {
                Thread.sleep(1000);
                System.out.println(threadName);
            } catch (InterruptedException ignored) {
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println(threadName + " 获取锁失败");
        }
    }

    public void tryLockWithTimeOutDemo(Lock lock) {
        String threadName = Thread.currentThread().getName();
        try {
            if (lock.tryLock(500, TimeUnit.MILLISECONDS)) {
                try {
                    Thread.sleep(1000);
                    System.out.println(threadName);
                } catch (InterruptedException ignored) {
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println(threadName + " 获取锁超时");
            }
        } catch (InterruptedException e) {
            System.out.println(threadName + " 线程被中断");
        }
    }
}
