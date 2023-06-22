package second_week;

//import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * VM Args：-Xmx20m  -XX:MaxDirectMemorySize=128k
 * <p>
 * MaxDirectMemorySize 默认与 Xmx 大小一致
 */
public class DirectMemoryOOM {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) throws Exception {
//        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
//        unsafeField.setAccessible(true);
//        Unsafe unsafe = (Unsafe) unsafeField.get(null);
//        while (true) {
//            unsafe.allocateMemory(_1MB);
//        }
    }
}
