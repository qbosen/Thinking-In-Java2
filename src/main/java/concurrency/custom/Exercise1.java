package concurrency.custom;

import org.junit.Test;

/**
 * @author qiubaisen
 * @date 2018-12-19
 */

class YieldRun implements Runnable {
    private static int count = 0;
    private int id = count++;
    private boolean isYield;

    public YieldRun(boolean yield) {
        isYield = yield;
        System.out.println("START id:" + id);
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            print(i);
            if (isYield)
                Thread.yield();
        }
        System.out.println("END id:" + id);
    }

    private void print(int stage) {
        System.out.println("id:" + id + "\t stage:" + stage);
    }

}

/**
 * 让步的结果中 同id相邻两个stage 中间大概率会穿插其他结果
 * 不让步的 相邻两个结果 大概率来自同一线程
 */
public class Exercise1 {
    @Test
    public void run5y() {
        for (int i = 0; i < 5; i++) {
            new Thread(new YieldRun(true)).start();
        }
    }

    @Test
    public void run5n() {
        for (int i = 0; i < 5; i++) {
            new Thread(new YieldRun(false)).start();
        }
    }
}
