package com.company.alanjager;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by AlanJager on 2017/1/24.
 */
public class ThreadPool {
    ConcurrentLinkedQueue<Task> workQueue = new ConcurrentLinkedQueue<Task>();
    Vector<WorkThread> vector = new Vector<WorkThread>();
    int poolSize = 10;

    public Vector<WorkThread> getVector() {
        return vector;
    }

    public void execute(WorkThread t) {
        if (vector.size() < poolSize) {
            if (!addWorker(t)) {
                System.out.println("add worker failed");
            }
        }

    }

    public synchronized boolean addWorker(WorkThread t) {
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
