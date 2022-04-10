package third_week;

public class ThreadDemo {

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread currentThread = Thread.currentThread();
            System.out.println("当前线程：" + currentThread.getName());
        };

        Thread thread = new Thread(task);
        thread.setName("test-thread-1");
        thread.setDaemon(true);
        thread.start();
//        Thread.sleep(6000);
    }

}
