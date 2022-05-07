package fourth_week;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParallelStreamDemo {
    public static void main(String[] args) {
        // 初始化 Integer List
        List<Integer> integers = new ArrayList<>();
        IntStream.range(1, 1000).forEach(integers::add);

        // 将 Integer List 转化为 Long List（并行）
        List<Long> longs = integers.stream().parallel().map(Integer::longValue).sorted().collect(Collectors.toList());

        // 将 Long List 放入阻塞式队列（并行）
        BlockingQueue<Long> blockingQueue = new LinkedBlockingQueue<>(1000);
        longs.stream().parallel().forEach(i -> {
            try {
                blockingQueue.put(i);
            } catch (InterruptedException ignored) {
            }
        });
        System.out.println(blockingQueue);
    }
}
