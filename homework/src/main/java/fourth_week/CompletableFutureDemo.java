package fourth_week;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class CompletableFutureDemo {
    public static void main(String[] args) throws Exception {
        final int threadNum = 8;
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNum);
        List<CompletableFuture<Void>> futureList = new ArrayList<>(threadNum);

        for (int i = 0; i < threadNum; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(new CyclicBarrierTask(cyclicBarrier));
            futureList.add(future);
        }

        for (CompletableFuture<Void> future : futureList) {
            future.get();
            System.out.println("线程返回成功");
        }
    }

    static class CyclicBarrierTask implements Runnable {

        private final CyclicBarrier cyclicBarrier;

        public CyclicBarrierTask(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            int millis = new Random().nextInt(5000);
            String threadName = Thread.currentThread().getName();
            try {
                System.out.println("线程 " + threadName + " 开始运行。是否守护线程：" + Thread.currentThread().isDaemon());
                TimeUnit.MILLISECONDS.sleep(millis);
                System.out.println("线程 " + threadName + " 开始等待");
                this.cyclicBarrier.await();
                System.out.println("线程 " + threadName + " 再次运行");
                TimeUnit.MILLISECONDS.sleep(millis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
