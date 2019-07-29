package me.dang.chapter07.load;

/**
 * 对于数组实例来说，其类型是由JVM在运行期动态生成的，表示为:
 * [Lme.dang.chapter07.load.TestClass
 * 这种形式。动态生成的类型，其父类型就是Object
 *
 * 对于数组来说，JavaDoc经常将构成数组的元素为Component，实际上就是将数组降低一个维度后的类型
 *
 * 使用 javap -c [class路径] 查看字节码信息
 *
 * 助记符:
 * anewarray: 表示创建一个引用类型的（如类、接口、数组）数组，并将其引用值压入栈顶
 * newarray: 表示创建一个指定的原始类型的（如int、double、char等）数组，并将其引用值压入栈顶
 * @author dht
 * @date 29/07/2019
 */
public class Test04 {

    public static void main(String[] args) {
        TestClass[] testClass_s = new TestClass[1];
        System.out.println(testClass_s.getClass());
        System.out.println(testClass_s.getClass().getSuperclass());

        TestClass[][] testClasses = new TestClass[1][1];
        System.out.println(testClasses.getClass());
        System.out.println(testClasses.getClass().getSuperclass());

        System.out.println("-----------------------------------------------");

        int[] ints = new int[1];
        System.out.println(ints.getClass());
        System.out.println(ints.getClass().getSuperclass());

        char[] chars = new char[1];
        System.out.println(chars.getClass());

        float[] floats = new float[1];
        System.out.println(floats.getClass());

        double[] doubles = new double[1];
        System.out.println(doubles.getClass());

        byte[] bytes = new byte[1];
        System.out.println(bytes.getClass());

        String[] strings = new String[1];
        System.out.println(strings.getClass());
    }

}

class TestClass {

    static {
        System.out.println("TestClass init!");
    }

}
