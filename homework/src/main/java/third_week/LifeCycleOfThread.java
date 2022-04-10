package third_week;

public class LifeCycleOfThread {

    public static void main(String[] args) throws Exception {
        LifeCycleOfThread lifeCycleOfThread = new LifeCycleOfThread();
        lifeCycleOfThread.newState();
        lifeCycleOfThread.runnableState();
        lifeCycleOfThread.blockedState();
        lifeCycleOfThread.waitingState();
    }

    private void newState() {
        Runnable runnable = () -> {
            // do something
        };
        Thread t = new Thread(runnable);
        System.out.println(t.getState());
    }

    private void runnableState() {
        Runnable runnable = () -> {
            // do something
        };
        Thread t = new Thread(runnable);
        t.start();
        System.out.println(t.getState());
    }

    private void blockedState() throws InterruptedException {
        Runnable runnable = LifeCycleOfThread::commonResource;

        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);

        t1.start();
        t2.start();

        Thread.sleep(1000);
        System.out.println(t2.getState());
        System.exit(0);
    }

    private static synchronized void commonResource() {
        while (true) {
            // do something
        }
    }

    private Thread t1;

    private void waitingState() {
        Runnable t1Runnable = () -> {
            Runnable t2Runnable = () -> {
                try {
                    Thread.sleep(1000);
                    System.out.println(this.t1.getState());
                } catch (InterruptedException ignored) {
                }
            };

            try {
                Thread t2 = new Thread(t2Runnable);
                t2.start();
                t2.join();
            } catch (InterruptedException ignored) {
            }
        };
        t1 = new Thread(t1Runnable);
        t1.start();
    }
}
