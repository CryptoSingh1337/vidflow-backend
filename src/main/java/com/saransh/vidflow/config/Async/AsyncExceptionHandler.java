package com.saransh.vidflow.config.Async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * author: CryptoSingh1337
 */
@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.debug("Exception cause - {}", ex.getMessage());
        log.debug("Method name - {}", method.getName());
        for (Object param : params)
            log.debug("Parameter value - {}", param);
    }
}
