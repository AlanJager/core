package com.company.T;

import java.util.concurrent.Future;

/**
 * Created by zouye on 2017/2/6.
 */
public interface Interface {
    <T> Future<T> submit();
}
