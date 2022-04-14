package third_week;

public class ThreadSafety {

    public static void main(String[] args) throws InterruptedException {
        ThreadSafety threadSafety = new ThreadSafety();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                threadSafety.printA();
            }
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                threadSafety.printB();
            }
        });
        thread2.start();
    }

    // 如果多个实例方法都被 synchronized 修饰，那么同一个对象的这些方法之间就是互斥的
    public synchronized void printA() {
        try {
            Thread.sleep(500);
            System.out.println("this is printA method");
        } catch (InterruptedException ignore) {
        }
    }

    public synchronized void printB() {
        try {
            Thread.sleep(500);
            System.out.println("this is printB method");
        } catch (InterruptedException ignored) {
        }
    }

    int x;
    int y;
    volatile boolean flag;

    private void volatileDemo() {
        // 对 volatile 变量进行操作，可以保证代码的有序性
        x = 2;          // 语句 1
        y = 0;          // 语句 2
        flag = true;    // 语句 3
        x = 4;          // 语句 4
        y = 1;          // 语句 5
        // 语句 1 和 2 不会被重排到 3 后面，4 和 5 也不会被重排到前面
        // 同时保证 1 和 2 的结果是对 3、4、5 可见（因为执行到 3 会刷新一遍主内存）
    }
}
