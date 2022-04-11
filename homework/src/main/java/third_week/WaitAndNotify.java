package third_week;

public class WaitAndNotify {

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
        if (threadName.equals("thread 1") || threadName.equals("thread 2")) {
            wait();
        }
        System.out.println(threadName);
        if (threadName.equals("thread 3")) {
            notify();
        }
    }

    // 同步静态方法，监听器属于 class 对象
    public synchronized static void syncStaticMethod() throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        if (threadName.equals("thread 1") || threadName.equals("thread 2")) {
            WaitAndNotify.class.wait();
        }
        System.out.println(threadName);
        if (threadName.equals("thread 3")) {
            WaitAndNotify.class.notify();
        }
    }

    // 同步代码块，监听器属于传入的对象
    public void syncBlock(Object obj) throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        synchronized (obj) {
            if (threadName.equals("thread 1") || threadName.equals("thread 2")) {
                obj.wait();
            }
            System.out.println(threadName);
            if (threadName.equals("thread 3")) {
                obj.notify();
            }
        }
    }
}
