package me.dang.chapter03;

/**
 * 大多数情况下，对象在新生代Eden区中分配。当Eden区没有足够空间进行分配时，
 * 虚拟机将发起一次Minor GC
 *
 * Minor GC：指发生在新生代的垃圾收集动作，因为Java对象大多都具备朝生夕灭
 *           的特性，所以Minor GC非常频繁，一般回收速度也很快
 * Major GC/Full GC：指发生在老年代的GC，出现了Major GC，经常会伴随至少一次的
 *           Minor GC（但并非绝对，在Parallel Scanvenge收集器的收集策略里就有
 *           直接进行Major GC的策略选择过程）。Major GC的速度一般会比Minor GC
 *           慢10倍以上
 *
 * 注意：为确保测试生效，可以添加-XX:+UseParNewGC参数
 *
 * VM Args: -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
 * @author dht
 * @date 21/07/2019
 */
public class TestAllocation {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        testAllocation();
    }

    public static void testAllocation() {
        byte[] allocation1, allocation2, allocation3, allocation4, allocation5;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        allocation4 = new byte[4 * _1MB]; //出现一次MinorGC
    }

}
