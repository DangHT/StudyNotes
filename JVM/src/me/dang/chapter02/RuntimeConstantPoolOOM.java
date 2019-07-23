package me.dang.chapter02;

import java.util.ArrayList;
import java.util.List;

/**
 * JDK1.6及之前的版本中，由于常量池分配在永久代中，所以可以通过设置永久代大小测试PermGen OOM
 * 从JDK1.7往后，常量池分配在堆中，则以下代码会一直循环下去
 * JDK1.8开始彻底放弃永久代，改用元空间
 *
 * VM Args: -XX:PermSize=10M -XX:MaxPermSize=10M (JDK7)
 *          -XX:MetaspaceSize=10M -XX:MaxMetaspaceSize=10M (JDK8)
 * @author dht
 * @date 17/07/2019
 */
public class RuntimeConstantPoolOOM {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        int i = 0;
        while (true) {
            list.add(String.valueOf(i++).intern());
        }
    }

}
