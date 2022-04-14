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
        x = 2;
        y = 0;
        flag = true;
        x = 4;
        y = 1;
    }
}
