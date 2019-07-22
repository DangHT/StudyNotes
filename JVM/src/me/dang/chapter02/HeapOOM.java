package me.dang.chapter02;

import java.util.ArrayList;
import java.util.List;

/**
 * Java堆用于存储对象实例，只要不断地创建对象，并且保证GC Roots到对象之间有可达路径
 * 来避免垃圾回收机制清除这些对象，那么这些对象数量达到最大堆的容量限制后就会产生内存
 * 溢出异常
 * VM Args: -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 * @author dht
 * @date 17/07/2019
 */
public class HeapOOM {

    static class OOMObject {}

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        int i = 0;

        try {
            while (true) {
                i++;
                list.add(new OOMObject());
            }
        } catch (OutOfMemoryError error) {
            System.out.println("共创建了 " + i + " 个OOMObject对象");
        }
    }

}
