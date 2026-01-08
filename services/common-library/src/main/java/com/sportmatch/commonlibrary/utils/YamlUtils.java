package com.sportmatch.commonlibrary.utils;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class YamlUtils {

    private YamlUtils() {}

    private static final Map<String, Map<String, Object>> CACHE = new HashMap<>();

    public static Map<String, Object> loadYaml(String fileName) {
        if (CACHE.containsKey(fileName)) {
            return CACHE.get(fileName);
        }

        InputStream inputStream = YamlUtils.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            log.error("YAML file {} not found", fileName);
            return Collections.emptyMap();
        }

        Yaml yaml = new Yaml();
        Map<String, Object> result = yaml.load(inputStream);
        CACHE.put(fileName, result);
        return result;
    }
}