package me.dang.chapter07.load;

/**
 * 关于此案例请参看(https://danght.github.io/2019/07/14/An-Example-Of-JVM.html)
 * @author dht
 * @date 30/07/2019
 */
public class Test06 {

    public static void main(String[] args) {
        Singleton singleton = Singleton.getInstance();

        System.out.println("counter1: " + Singleton.counter1);
        System.out.println("counter2: " + Singleton.counter2);
    }

}

class Singleton {

    public static int counter1 = 1;


    private static Singleton singleton = new Singleton();

    private Singleton() {
        counter1++;
        counter2++;

        System.out.println("counter1: " + counter1);
        System.out.println("counter2: " + counter2);
    }

    public static int counter2 = 0;

    public static Singleton getInstance() {
        return singleton;
    }

}