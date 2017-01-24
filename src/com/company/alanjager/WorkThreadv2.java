package com.company.alanjager;

/**
 * Created by AlanJager on 2017/1/24.
 */
public class WorkThreadv2 extends Thread {

    Task task;

    private void process() {
        System.out.println("process argument: " + task.getArgument());
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+" Start. argument = " + this.task.getArgument());
        processCommand();
        System.out.println(Thread.currentThread().getName()+" End.");
    }

    private void processCommand() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
