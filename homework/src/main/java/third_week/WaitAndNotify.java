package third_week;

public class WaitAndNotify {

    private static boolean thread3isDone = false;

    public static void main(String[] args) throws Exception {
        WaitAndNotify waitAndNotify = new WaitAndNotify();
        Runnable syncMethodTask = () -> {
            try {
                waitAndNotify.syncMethod();
            } catch (InterruptedException ignored) {
            }
        };

        Runnable syncStaticMethodTask = () -> {
            try {
                syncStaticMethod();
            } catch (InterruptedException ignored) {
            }
        };

        Object object = new Object();
        Runnable syncBlockTask = () -> {
            try {
                waitAndNotify.syncBlock(object);
            } catch (InterruptedException ignored) {
            }
        };

        Thread t1 = new Thread(syncMethodTask, "thread 1");
        t1.start();

        Thread t2 = new Thread(syncMethodTask, "thread 2");
        t2.start();

        Thread t3 = new Thread(syncMethodTask, "thread 3");
        t3.start();
    }

    // 同步实例方法，监听器属于实例对象
    public synchronized void syncMethod() throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        while ((threadName.equals("thread 1") || threadName.equals("thread 2")) && !thread3isDone) {
            wait();
        }
        System.out.println(threadName);
        if (threadName.equals("thread 3")) {
            thread3isDone = true;
            notifyAll();
        }
    }

    // 同步静态方法，监听器属于 class 对象
    public synchronized static void syncStaticMethod() throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        while ((threadName.equals("thread 1") || threadName.equals("thread 2")) && !thread3isDone) {
            WaitAndNotify.class.wait();
        }
        System.out.println(threadName);
        if (threadName.equals("thread 3")) {
            thread3isDone = true;
            WaitAndNotify.class.notifyAll();
        }
    }

    // 同步代码块，监听器属于传入的对象
    public void syncBlock(Object obj) throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        synchronized (obj) {
            while ((threadName.equals("thread 1") || threadName.equals("thread 2")) && !thread3isDone) {
                obj.wait();
            }
            System.out.println(threadName);
            if (threadName.equals("thread 3")) {
                thread3isDone = true;
                obj.notifyAll();
            }
        }
    }
}
