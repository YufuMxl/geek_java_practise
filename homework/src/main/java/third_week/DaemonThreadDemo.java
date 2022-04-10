package third_week;

public class DaemonThreadDemo {

    public static void main(String[] args) {
        Runnable task = () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread currentThread = Thread.currentThread();
            System.out.println("当前线程：" + currentThread.getName());
        };

        // 创建守护线程
        Thread thread = new Thread(task);
        thread.setName("test-thread-1");
        thread.setDaemon(true);
        // 运行守护线程
        thread.start();
    }

}
