package com.huihui.exceptions;

/**
 * Created by hadoop on 2015/7/31 0031.
 */
public class ExceptionFactory {
    public static RuntimeException wrapException(String message, Exception e) {
        throw  new RuntimeException(e);
    }
}
