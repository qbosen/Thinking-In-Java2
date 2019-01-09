package concurrency.custom;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author qiubaisen
 * @date 2019-01-09
 */
public class Exercise21 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Coop1 coop1 = new Coop1();
        Coop2 coop2 = new Coop2(coop1);
        executorService.execute(coop1);
        executorService.execute(coop2);
        TimeUnit.SECONDS.sleep(2);
        executorService.shutdown();
    }
}


class Coop1 implements Runnable {
    @Override
    public void run() {
        System.out.println("coop1 will wait");
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("coop1 end wait");
        }
    }
}

class Coop2 implements Runnable {
    private final Runnable coop1;

    public Coop2(Runnable coop1) {
        this.coop1 = coop1;
    }

    @Override
    public void run() {
        System.out.println("coop2 start");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("coop2 notify");
        synchronized (coop1) {
            coop1.notifyAll();
        }
        System.out.println("coop2 end");
    }
}