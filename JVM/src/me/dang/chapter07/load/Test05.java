package me.dang.chapter07.load;

import java.util.Random;

/**
 * 当一个接口在初始化时，并不要求其父接口都完成了初始化
 * 只有只有在真正使用到父接口的时候（如引用接口中定义的常量）才会初始化
 * @author dht
 * @date 29/07/2019
 */
public class Test05 {

    public static void main(String[] args) {
        System.out.println(Child05.b);
    }

}

interface Parent05 {

    int a = new Random().nextInt(3);

}

interface Child05 extends Parent05 {

    int b = 5;

}
