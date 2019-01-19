//: concurrency/DeadlockingDiningPhilosophers.java
package concurrency; /* Added by Eclipse.py */
// Demonstrates how deadlock can be hidden in a program.
// {Args: 0 5 timeout}

import custom.Utils;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DeadlockingDiningPhilosophers {
    public static void main(String[] args) throws Exception {
        int ponder = 5;
        if (args.length > 0)
            ponder = Integer.parseInt(args[0]);
        int size = 5;
        if (args.length > 1)
            size = Integer.parseInt(args[1]);
        ExecutorService exec = Executors.newCachedThreadPool();
        Chopstick[] sticks = new Chopstick[size];
        for (int i = 0; i < size; i++)
            sticks[i] = new Chopstick();
        for (int i = 0; i < size; i++)
            exec.execute(new Philosopher(
                    sticks[i], sticks[(i + 1) % size], i, ponder));
        if (args.length == 3 && args[2].equals("timeout"))
            TimeUnit.SECONDS.sleep(5);
        else {
            System.out.println("Press 'Enter' to quit");
            System.in.read();
        }
        exec.shutdownNow();
    }

    @Test
    public void normal() {
        test(20, 5, false);
    }

    @Test
    public void dead() {
        test(0, 5, true);
    }


    private void test(int ponder, int size, boolean timeout) {
        try {
            main(Utils.toArr(
                    String.valueOf(ponder),
                    String.valueOf(size),
                    timeout ? "timeout" : ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} /* (Execute to see output) *///:~
