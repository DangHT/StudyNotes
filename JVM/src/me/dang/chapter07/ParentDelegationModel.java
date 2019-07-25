package me.dang.chapter07;

/**
 * 从Java虚拟机的角度来讲，只存在两种不同类型的类加载器：
 * 1）启动类加载器（Bootstrap ClassLoader），这个类加载器使用C++语言实现，是JVM自身的一部分
 * 2）其他所有类加载器，由Java语言实现，独立于JVM外部，并且都继承自抽象类java.lang.ClassLoader
 *
 * 从开发者角度来看，类加载器可以分为三种：
 * 1）启动类加载器（同上），它负责加载%JAVA_HOME%\lib中的，或是被-Xbootclasspath指定路径中
 *    可被JVM识别的类库，如%JAVA_HOME%\lib\rt.jar
 * 2）扩展类加载器（Extension ClassLoader），它负责加载%JAVA_HOME%\lib\ext目录中的。或是被
 *    java.ext.dirs系统变量所指定的路径中的所有类库，开发者可以直接使用扩展类加载器
 * 3）应用程序类加载器（Application ClassLoader），它负责加载用户类路径CLASSPATH中指定的类库，
 *    开发者可以直接使用这个类加载器，如果应用程序中没有自定义的类加载器，一般默认使用这个
 *
 *                  -------------------------
 *                 |      启动类加载器       |
 *                 |  Bootstrap ClassLoader |
 *                 --------------------------
 *                           /\
 *                           |
 *                 -------------------------
 *                |      扩展类加载器       |
 *                |  Extension ClassLoader |
 *                --------------------------
 *                           /\
 *                           |
 *                  -------------------------
 *                 |    应用程序类加载器     |
 *                 | Application ClassLoader|
 *                 --------------------------
 *                  /\                   /\
 *                  |                    |
 *   -------------------------          -------------------------
 *  |      自定义类加载器     |         |      自定义类加载器     |
 *  |    User ClassLoader    |         |    User ClassLoader    |
 *  --------------------------         --------------------------
 *                    类加载器双亲委派模型
 *
 * 双亲委派机制：如果一个类加载器收到了类加载请求，它首先不会自己去尝试加载这个类，而是把这个
 * 请求委派给父类加载器去完成，每一层次的类加载器都是如此，因此所有的加载请求最终都应该传送到
 * 顶层的启动类加载器中，只有当父加载器反馈自己无法完成加载（它的搜索范围中未找到所需的类）时
 * 子类才会尝试加载
 *
 * 双亲委派机制的主要目的是为了安全，例如：用户自定义一个java.lang.String类来迷惑JVM，在JVM
 * 对类进行加载时，会委托启动类加载器，当启动类加载器加载过JRE中的java.lang.String类后，便不
 * 会再去加载用户自定义的java.lang.String
 *
 * @author dht
 * @date 25/07/2019
 */
public class ParentDelegationModel {

    public static void main(String[] args) throws ClassNotFoundException {
        Object object = new Object();
        System.out.println("obj: " + object.getClass().getClassLoader());
        System.out.println();

        Class clazz = Class.forName("me.dang.chapter02.HeapOOM");
        System.out.println("HeapOOM: " + clazz.getClassLoader());
        System.out.println();

        ParentDelegationModel model = new ParentDelegationModel();
        System.out.println("model: " + model.getClass().getClassLoader());
        System.out.println("model's parent: " + model.getClass().getClassLoader().getParent());
        System.out.println("model's grandparent: " + model.getClass().getClassLoader().getParent().getParent());
    }

}
