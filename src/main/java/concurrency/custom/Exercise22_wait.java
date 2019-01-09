package concurrency.custom;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author qiubaisen
 * @date 2019-01-09
 */
public class Exercise22_wait {
    private static int spins;

    public static void main(String[] args) throws InterruptedException {
        Runnable changeState = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        return;
                    }
                    synchronized (this) {
                        notify();
                    }
                }
            }
        };

        Runnable busyWait = () -> {
            while (true) {
                synchronized (changeState) {
                    try {
                        changeState.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                    System.out.println("cycled");
                }
            }
        };

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(changeState);
        executorService.execute(busyWait);
        TimeUnit.SECONDS.sleep(1);
        executorService.shutdownNow();
    }
}

