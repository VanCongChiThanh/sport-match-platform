package com.sportmatch.commonlibrary.utils;

import java.util.regex.Pattern;

public final class StringUtils {
    private StringUtils() {}

    public static String toSnakeCase(String input) {
        return input.replaceAll("([^_A-Z])([A-Z])", "$1_$2").toLowerCase();
    }

    public static String escapeRegex(String content) {
        return content == null ? null : Pattern.quote(content);
    }
}