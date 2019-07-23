package me.dang.chapter02;

/**
 * 如果测试时不限于单线程，通过不断地建立线程的方式倒是可以产生内存溢出异常。
 * 但是这样产生的内存溢出异常与栈空间是否足够大并不存在任何联系，或者准确的说
 * 在这种情况下，为每个线程的栈分配的内存越大，反而越容易产生内存溢出异常。
 *
 * 注意：本测试用例可能会导致死机，请谨慎运行！
 *
 * VM Args: -Xss2M
 * @author dht
 * @date 23/07/2019
 */
public class JavaVMStackOOM {

    private void dontStop() {
        while (true) {}
    }

    public void stackLeakByThread() {
        while (true) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    dontStop();
                }
            });
            thread.start();
        }
    }

    public static void main(String[] args) {
        JavaVMStackOOM oom = new JavaVMStackOOM();
        oom.stackLeakByThread();
    }

}
