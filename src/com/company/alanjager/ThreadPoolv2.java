package com.company.alanjager;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by AlanJager on 2017/1/24.
 */
public class ThreadPoolv2 {
    ConcurrentLinkedQueue<Task> workQueue = new ConcurrentLinkedQueue<Task>();
    Vector<WorkThreadv2> vector = new Vector<WorkThreadv2>();
    int poolSize = 10;

    public Vector<WorkThreadv2> getVector() {
        return vector;
    }

    public void execute(WorkThreadv2 t) {
        if (vector.size() < poolSize) {
            if (!addWorker(t)) {
                System.out.println("add worker failed");
            }
        }

    }

    public synchronized boolean addWorker(WorkThreadv2 t) {
        t.task = takeTask();
        t.start();
        return true;
    }

    private Task takeTask() {
        if (workQueue == null || workQueue.isEmpty()) {
            return null;
        }

        return workQueue.poll();
    }

    public void addTask(Task t) {
        workQueue.add(t);
    }
}
