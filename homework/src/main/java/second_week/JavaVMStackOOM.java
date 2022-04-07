package second_week;

/**
 * VM Args：-Xss512M（该值越大，越容易 OOM）
 */
public class JavaVMStackOOM {
    private void dontStop() {
        while (true) {
        }
    }

    public void stackLeakByThread() {
        while (true) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {dontStop();}
            });
            thread.start();
        }
    }

    /**
     * 容易导致死机，请谨慎执行
     */
    public static void main(String[] args) {
        JavaVMStackOOM oom = new JavaVMStackOOM();
        oom.stackLeakByThread();
    }
}
