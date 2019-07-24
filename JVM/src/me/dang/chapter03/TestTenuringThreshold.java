package me.dang.chapter03;

/**
 * 虚拟机给每个对象定义了一个对象年龄（Age）计数器。
 * 如果对象在Eden出生并经过第一次Minor GC后仍然存活，并且能够被Survivor容纳的话，
 * 将被移动到Survivor空间中，并且对象年龄设为1。对象在Survivor区中每熬过一次Minor GC
 * 年龄就增加一岁，当它的年龄增加到一定的程度（默认15岁），就会被晋升到老年代中。
 *
 * 完成本测试用例，应当分别设置-XX:MaxTenuringThreshold参数为1和15，观察输出区别
 *
 * VM Args: -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails
 *          -XX:SurvivorRatio=8
 *          -XX:MaxTenuringThreshold=1
 * @author dht
 * @date 23/07/2019
 */
public class TestTenuringThreshold {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        testTenuringThreshold();
    }

    public static void testTenuringThreshold() {
        byte[] allocation1, allocation2, allocation3;
        allocation1 = new byte[_1MB / 4];
        //什么时候进入老年代去取决于-XX:MaxTenuringThreshold设置
        allocation2 = new byte[4 * _1MB];
        allocation3 = new byte[4 * _1MB];
        allocation3 = null;
        allocation3 = new byte[4 * _1MB];
    }

}
