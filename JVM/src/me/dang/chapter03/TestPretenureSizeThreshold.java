package me.dang.chapter03;

/**
 * 所谓的大对象是指，需要大量连续内存空间的Java对象，最典型的大对象就是那种
 * 很长的字符串以及数组。经常出现大对象容易导致内存还有不少空间时就提前触发
 * 垃圾收集以获取足够的连续空间来“安置”它们。
 * 虚拟机提供了一个-XX:PretenureSizeThreshold参数，令大于这个设置值的对象
 * 直接在老年代分配。这样做的目的是避免在Eden区以及两个Survivor区之间发生
 * 大量的内存复制。
 *
 * 注意：PretenureSizeThreshold参数只对Serial和ParNew两款收集器有效，
 *      Parallel Scavenge收集器不认识这个参数，它一般不需要设置。
 *      如果确保本测试生效，可以添加参数-XX:+UseParNewGC
 *
 * VM Args: -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails
 *          -XX:SurvivorRatio=8
 *          -XX:PretenureSizeThreshold=31452728
 * @author dht
 * @date 23/07/2019
 */
public class TestPretenureSizeThreshold {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        testPretenureSizeThreshold();
    }

    public static void testPretenureSizeThreshold() {
        byte[] allocation;
        allocation = new byte[4 * _1MB]; //直接分配在老年代中
    }

}
