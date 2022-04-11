package third_week;

public class InterruptDemo {

    private static boolean isInterrupted = false;
    private static boolean isBroken = false;

    public static void main(String[] args) throws InterruptedException {
        // 以下代码演示即时中断
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Thread 1 Interrupted");
            }
        });
        thread1.start();
        thread1.interrupt();

        // 以下代码演示延迟中断
        Thread thread2 = new Thread(() -> {
            while (!isInterrupted) {
                System.out.println("Thread 2 is running");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Thread 2 Interrupted");
            }
        });
        thread2.start();
        thread2.interrupt();
        Thread.sleep(1);
        isInterrupted = true;

        // 以下代码演示无效 interrupt()
        Thread thread3 = new Thread(() -> {
            while (!isBroken) {
                System.out.println("Thread 3 is running");
            }
        });
        thread3.start();
        thread3.interrupt();
        Thread.sleep(1);
        isBroken = true;
    }
}
