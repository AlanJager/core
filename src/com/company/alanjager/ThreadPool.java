package com.company.alanjager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by AlanJager on 2017/1/24.
 */
public class ThreadPool {
    private static int worker_num = 5;
    private WorkThread[] workThreads;
    private static volatile int unfinished_task = 0;
    private List<Runnable> taskQueue = new LinkedList<Runnable>();
    private static ThreadPool threadPool;
    private static volatile int finished_task = 0;

    private ThreadPool() {
        this(5);
    }

    private ThreadPool(int worker_num) {
        ThreadPool.worker_num = worker_num;
        workThreads = new WorkThread[worker_num];
        for (int i = 0; i < worker_num; i++) {
            workThreads[i] = new WorkThread();
            workThreads[i].start();
        }
    }

    public static ThreadPool getThreadPool() {
        return getThreadPool(ThreadPool.worker_num);
    }

    public static ThreadPool getThreadPool(int worker_num) {
        if (worker_num <= 0) {
            worker_num = ThreadPool.worker_num;
        } else if (threadPool == null) {
            threadPool = new ThreadPool(worker_num);
        }

        return threadPool;
    }

    public void execute(Runnable r) {
        synchronized (this.taskQueue) {
            taskQueue.add(r);
            taskQueue.notify();
        }
    }

    public static int getWorker_num() {
        return worker_num;
    }

    public void execute(Runnable[] r) {
        synchronized (this.taskQueue) {
            for (Runnable task : r) {
                taskQueue.add(task);
            }
            taskQueue.notify();
        }

    }
    public void destroy() {
        while (!taskQueue.isEmpty()) {// 如果还有任务没执行完成，就先睡会吧
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 工作线程停止工作，且置为null
        for (int i = 0; i < worker_num; i++) {
            workThreads[i].stopWorker();
            workThreads[i] = null;
        }
        threadPool=null;
        taskQueue.clear();// 清空任务队列
    }


    private class WorkThread extends Thread {
        // 该工作线程是否有效，用于结束该工作线程
        private boolean isRunning = true;

        /*
         * 关键所在啊，如果任务队列不空，则取出任务执行，若任务队列空，则等待
         */
        @Override
        public void run() {
            Runnable r = null;
            while (isRunning) {// 注意，若线程无效则自然结束run方法，该线程就没用了
                synchronized (taskQueue) {
                    while (isRunning && taskQueue.isEmpty()) {// 队列为空
                        try {
                            taskQueue.wait(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!taskQueue.isEmpty())
                        r = taskQueue.remove(0);// 取出任务
                }
                if (r != null) {
                    System.out.println(Thread.currentThread().getName() + " is working");
                    r.run();// 执行任务
                }
                finished_task++;
                r = null;
            }
        }

        // 停止工作，让该线程自然执行完run方法，自然结束
        public void stopWorker() {
            isRunning = false;
        }
    }

    public static void main(String[] args) {
        // 创建3个线程的线程池
        ThreadPool t = ThreadPool.getThreadPool(3);
        for (int i = 0; i < 10; i ++) {
            t.execute(new Task());
        }
        System.out.println(t);
        t.destroy();// 所有线程都执行完成才destory
        System.out.println(t);
    }

    // 任务类
    static class Task implements Runnable {
        private static volatile int i = 1;

        @Override
        public void run() {// 执行任务
            System.out.println("任务 " + (i++) + " 完成");
        }
    }
}
