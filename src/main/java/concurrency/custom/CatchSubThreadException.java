package concurrency.custom;

import org.junit.Test;

/**
 * @author qiubaisen
 * @date 2018-12-20
 */
public class CatchSubThreadException {
    @Test
    public void test() {
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler =
                (t, e) -> System.out.println("get it! message:" + e.getMessage());
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new RuntimeRun());
            thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
            thread.start();
        }
    }
}

class RuntimeRun implements Runnable {
    static int count = 0;
    int id = count++;

    @Override
    public void run() {
        System.out.println("run " + id);

        throw new RuntimeException("oh no " + id);
    }

}


