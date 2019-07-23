package me.dang.chapter02;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 方法区用于存放Class的相关信息，如类名、访问修饰符、常量池、字段描述、
 * 方法描述等。对于这些区域的测试，基本的思路是运行时产生大量的类去填满
 * 方法区，直到溢出。
 * 本次测试用例借助CGLib直接操作字节码运行时产生了大量的动态类
 *
 * VM Args: -XX:PermGenSize=10M -XX:MaxPermGenSize=10M (JDK1.7)
 *          -XX:MetaspaceSize=10M -XX:MaxMetaspaceSize=10M (JDK1.8)
 * @author dht
 * @date 23/07/2019
 */
public class JavaMethodAreaOOM {

    static class OOMObject {}

    public static void main(String[] args) {
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(OOMObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    return methodProxy.invokeSuper(o, args);
                }
            });
            enhancer.create();
        }
    }

}
