package me.dang.chapter03;

/**
 * 在发生Minor GC之前，虚拟机会先检查老年代最大可用的连续空间是否大于新生代所有对象总空间
 * 如果这个条件成立，那么可以Minor GC可以确保是安全的。否则，虚拟机会查看HandlePromotionFailure
 * 设置值是否允许担保失败。如果允许，则会继续检查老年代最大可用的连续空间是否大于历次晋升
 * 到老年代对象的平均大小，如果大于，将尝试一次Minor GC，尽管这次Minor GC可能有风险；如果
 * 小于，或者HandlePromotionFailure设置为不允许冒险，那这时也要改为进行一次Full GC
 * 冒险是指，在新生代使用复制收集算法时，为了内存利用率，只使用其中一个Survivor空间作为轮换
 * 备份，因此当出现大量对象在Minor GC后仍然存活的情况，就需要老年代进行分配担保，把Survivor
 * 无法容纳的对象直接进入老年代。
 *
 * 注意：此测试用例仅供参考，JDK 6 Update 24之后，HandlePromotionFailure参数不会再影响到
 *      虚拟机的空间分配担保策略了
 *
 * VM Args:-Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
 *         -XX:-HandlePromotionFailure
 * @author dht
 * @date 24/07/2019
 */
public class TestHandlePromotion {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        testHandlePromotion();
    }

    public static void testHandlePromotion() {
        byte[] allocation1, allocation2, allocation3, allocation4,
               allocation5, allocation6, allocation7;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        allocation1 = null;
        allocation4 = new byte[2 * _1MB];
        allocation5 = new byte[2 * _1MB];
        allocation6 = new byte[2 * _1MB];
        allocation4 = null;
        allocation5 = null;
        allocation6 = null;
        allocation7 = new byte[2 * _1MB];
    }

}
