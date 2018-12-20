//: concurrency/MainThread.java
package concurrency; /* Added by Eclipse.py */

public class MainThread {
    public static void main(String[] args) {
        System.out.println("current thread name:" + Thread.currentThread().getName());
        LiftOff launch = new LiftOff();
        // 直接在当前线程执行
        launch.run();
        System.out.println();
        // 新线程执行
        new Thread(new LiftOff()).start();

    }
} /* Output:
#0(9), #0(8), #0(7), #0(6), #0(5), #0(4), #0(3), #0(2), #0(1), #0(Liftoff!),
*///:~
