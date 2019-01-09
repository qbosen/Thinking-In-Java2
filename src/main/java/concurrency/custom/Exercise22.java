package concurrency.custom;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author qiubaisen
 * @date 2019-01-09
 */
public class Exercise22 {
    private static volatile boolean flag = false;
    private static int spins;

    public static void main(String[] args) throws InterruptedException {
        Runnable changeState = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    return;
                }
                flag = true;
            }
        };

        Runnable busyWait = () -> {
            while (true) {
                while (!flag && !Thread.currentThread().isInterrupted()) {
                    spins++;
                }
                System.out.println("spins:" + spins);
                spins = 0;
                flag = false;

                if (Thread.currentThread().isInterrupted()) {
                    return;
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

