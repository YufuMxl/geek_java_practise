package fourth_week;

/**
 * volatile 变量自增运算测试
 */
public class VolatileTest {

    // volatile 修饰的变量不具备原子性
    public static volatile int race = 0;

    public static void increase() {
        race++;
    }

    private static final int THREADS_COUNT = 20;

    public static void main(String[] args) throws Exception {
        for (Thread myThread : new Thread[THREADS_COUNT]) {
            myThread = new Thread(() -> {
                for (int i = 0; i < 10000; i++) {
                    increase();
                }
            });
            myThread.start();
        }

        Thread.sleep(2000);
        System.out.println(race);
    }
}


