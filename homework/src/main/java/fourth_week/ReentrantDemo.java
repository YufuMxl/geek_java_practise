package fourth_week;

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
    }
}
