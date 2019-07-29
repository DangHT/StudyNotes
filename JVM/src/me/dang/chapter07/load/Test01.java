package me.dang.chapter07.load;

/**
 * 被动使用类字段演示一：
 * 通过子类引用父类的静态字段，不会导致子类初始化
 * 对于静态字段，只有直接定义这个字段的类才会被初始化
 * 至于是否要触发子类的加载和验证，在虚拟机规范中并未明确规定，这取决于虚拟机的具体实现
 * 对于Sun HotSpot来说，使用-XX:+TraceClassLoading可以发现子类SubClass也是被加载了的
 * @author dht
 * @date 26/07/2019
 */
public class Test01 {

    public static int value = 123;

    static {
        System.out.println("SuperClass init!");
    }

    public static void main(String[] args) {
        System.out.println(SubClass.value);
    }

}

class SubClass extends Test01 {

    static {
        System.out.println("SubClass init!");
    }

}
