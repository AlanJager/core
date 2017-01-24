package com.company.tpe;

import static java.lang.Thread.sleep;

/**
 * Created by AlanJager on 2017/1/24.
 */
public class WorkThread implements Runnable {

    private String command;

    public WorkThread(String s){
        this.command=s;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+" Start. Command = "+command);
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

    @Override
    public String toString(){
        return this.command;
    }
}
