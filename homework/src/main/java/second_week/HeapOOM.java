package second_week;

import java.util.ArrayList;
import java.util.List;

/**
 * VM Args：-Xmx20m -Xms20m -XX:+HeapDumpOnOutOfMemoryError
 */
public class HeapOOM {
    static class OOMObject {}

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject());
        }
    }
}
