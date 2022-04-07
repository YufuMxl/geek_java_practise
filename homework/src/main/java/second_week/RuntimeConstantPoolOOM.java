package second_week;

import java.util.HashSet;
import java.util.Set;

/**
 * VM Args：-Xmx6m   -Xms6m
 * <p>
 * 自 JDK 7 起，原本存放在永久代的常量池被转移到堆中。所以该代码并不会导致元数据区内存溢出，即设置 -XX:MaxMetaspaceSize=12m 无效
 */
public class RuntimeConstantPoolOOM {

    public static void main(String[] args) {
        // 使用 Set 保持着对常量的引用，避免 Full GC 回收常量
        Set<String> set = new HashSet<>();
        int i = 0;
        while (true) {
            set.add(String.valueOf(i++).intern());
        }
    }

}
