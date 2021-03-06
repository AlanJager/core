package com.company.tpe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by AlanJager on 2017/1/24.
 */

public class ThreadPool {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            Runnable worker = new WorkThread("" + i);
            executor.execute(worker);
        }
        executor.shutdown(); // This will make the executor accept no new threads and finish all existing threads in the queue
        while (!executor.isTerminated()) { // Wait until all threads are finish,and also you can use "executor.awaitTermination();" to wait
        }
        System.out.println("Finished all threads");
    }

}