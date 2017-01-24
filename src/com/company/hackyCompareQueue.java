package com.company;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
/**
 * Created by AlanJager on 2017/1/24.
 */
public class hackyCompareQueue {
}



class QueueDemo {
    private static final int NUM_NODES = 1000000;
    private static final int NUM_TRIALS = 10;
    private static final int NUM_THREADS = 8;
    private static final Object ANY_OBJECT = new Object();

    public static void main(String[] args) {
        Queue<Object> qN = new ConcurrentLinkedQueue<>();
        Queue<Object> qB = new LinkedBlockingQueue<>();
        Queue<Object> qA = new ArrayBlockingQueue<>(NUM_NODES+1);

        for (int i=0 ; i<NUM_TRIALS ; i++) {
            doOneTrial(qN, "non-blocking");
            doOneTrial(qB, "    blocking");
            doOneTrial(qA, "       Array");
        }
    }

    private static void doOneTrial(final Queue<Object> q, String name) {
        List<CompletableFuture<Integer>> futures = new ArrayList<>(NUM_THREADS);
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(NUM_THREADS);

        fillQueue(q);
        for (int i=0 ; i<NUM_THREADS ; i++) {
            futures.add(CompletableFuture.supplyAsync(new Supplier<Integer>() {
                public Integer get() {
                    int count = 0;
                    wait4(startSignal);
                    while (q.poll() != null) count++;
                    doneSignal.countDown();
                    return count;
                }
            }));
        }

        long startTime = System.currentTimeMillis();
        startSignal.countDown();
        wait4(doneSignal);
        long endTime =  System.currentTimeMillis();

        int count = 0;
        for (CompletableFuture<Integer> future : futures) {
            count += future.join();
        }
        if (count == NUM_NODES) {
            System.out.println(name + ", " + Long.toString(endTime-startTime));
        } else {
            System.out.println("Aieeeeeegh!");
            System.exit(1);
        }
    }

    private static void fillQueue(Queue<Object> q) {
        for (int i=0 ; i<NUM_NODES ; i++) {
            q.add(ANY_OBJECT);
        }
    }

    private static void wait4(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}