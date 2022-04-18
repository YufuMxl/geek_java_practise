package fourth_week;

/**
 * 线程死锁等待演示
 */
public class DeadLockDemo {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {
        Runnable task1 = () -> {
            // 占有 lock1 尝试获取 lock2
            synchronized (lock1) {
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + ":lock1" + "lock2");
                }
            }
        };

        Runnable task2 = () -> {
            // 占有 lock2 尝试获取 lock1
            synchronized (lock2) {
                synchronized (lock1) {
                    System.out.println(Thread.currentThread().getName() + ":lock2" + "lock1");
                }
            }
        };

        for (int i = 0; i < 100; i++) {
            new Thread(task1).start();
            new Thread(task2).start();
        }
    }
}
