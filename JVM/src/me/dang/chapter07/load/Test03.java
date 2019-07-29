package me.dang.chapter07.load;

import java.util.UUID;

/**
 * 当一个常量的值并非编译期间可以确定的，那么其值就不会被放到调用类的常量池中，
 * 这时在程序运行时，会导致主动使用这个常量所在的类，则该类就会被初始化
 * @author dht
 * @date 29/07/2019
 */
public class Test03 {

    public static void main(String[] args) {
        System.out.println(ConstClass_03.str);
    }

}

class ConstClass_03 {

    public static final String str = UUID.randomUUID().toString();

    static {
        System.out.println("ConstClass_03 init!");
    }

}
