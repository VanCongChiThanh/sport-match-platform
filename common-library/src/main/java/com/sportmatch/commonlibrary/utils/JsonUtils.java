package com.sportmatch.commonlibrary.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonUtils {
    private JsonUtils() {}
    public static String convertJsonToString(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}