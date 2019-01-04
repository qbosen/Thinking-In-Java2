package concurrency.custom;


import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static net.mindview.util.Print.print;

/**
 * @author qiubaisen
 * @date 2019-01-04
 */
class NeedsCleanup implements AutoCloseable {
    private final int id;

    public NeedsCleanup(int ident) {
        id = ident;
    }

    public void afterInit() {
        print("NeedsCleanup " + id);
    }

    @Override
    public void close() {
        print("Cleaning up " + id);
    }
}

@SuppressWarnings("Duplicates")
class Blocked implements Runnable {
    private volatile double d = 0.0;

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {

                try (NeedsCleanup n1 = new NeedsCleanup(1)) {
                    n1.afterInit();
                    print("Sleeping");
                    TimeUnit.SECONDS.sleep(1);

                    try (NeedsCleanup n2 = new NeedsCleanup(2)) {
                        n2.afterInit();
                        print("Calculating");
                        for (int i = 1; i < 2500000; i++)
                            d = d + (Math.PI + Math.E) / d;
                        print("Finished time-consuming operation");
                    }
                }
            }
            print("Exiting via while() test");
        } catch (InterruptedException e) {
            print("Exiting via InterruptedException");
        }
    }
}

public class InterruptingIdiom {
    public static void testWithTime(long milliseconds) throws Exception {
        Thread t = new Thread(new Blocked());
        t.start();
        TimeUnit.MILLISECONDS.sleep(milliseconds);
        t.interrupt();
    }

    @Test
    public void test() throws Exception {
        testWithTime(1100);
    }

    @Test
    public void test2() throws Exception {
        testWithTime(800);
    }
}

