package com.sportmatch.identityservice.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class LogUtils {

    private LogUtils() {}

    /**
     * Extract log message
     *
     * @param method Error method
     * @param uri Error uri
     * @param error Error message
     */
    public static void error(String method, String uri, String error) {
        String content = method + "/" + uri + " - " + "Error: " + error;
        log.error(content);
    }
}