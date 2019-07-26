package me.dang.chapter07.load;

/**
 * 被动使用类字段演示一：
 * 通过子类引用父类的静态字段，不会导致子类初始化
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
