package second_week;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class JOLTest {
    public static void main(String[] args) {
        // 查看虚拟机详情
        System.out.println(VM.current().details());

        // 查看空对象的内存布局
        EmptyClass emptyClass = new EmptyClass();
        System.out.println(ClassLayout.parseInstance(emptyClass).toPrintable());

        // 查看全字段对象的内存布局
        AllFieldClass allFieldClass = new AllFieldClass();
        System.out.println(ClassLayout.parseInstance(allFieldClass).toPrintable());

        // 查看数组对象的内存布局
        EmptyClass[] emptyClassArray = new EmptyClass[1];
        System.out.println(ClassLayout.parseInstance(emptyClassArray).toPrintable());
    }
}
