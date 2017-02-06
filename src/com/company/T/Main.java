package com.company.T;

import java.util.concurrent.Future;

/**
 * Created by zouye on 2017/2/6.
 */
public class Main<T> extends Super implements Interface {
    private T test;

    public T getTest() {
        return test;
    }

    public void setTest(T test) {
        this.test = test;
    }

    public static void main(String[] args) {
        Main<String> main = new Main<String>();
        main.setTest("Item");
        main.test.getClass();
        main.submit();
    }

    @Override
    public <T> Future<T> submit() {
        return null;
    }
}
