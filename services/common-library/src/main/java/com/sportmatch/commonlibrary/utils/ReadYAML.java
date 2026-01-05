package com.sportmatch.commonlibrary.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public final class ReadYAML {

  ReadYAML() {}

  public Map<String, Object> getValueFromYAML(String nameFile) {
    Yaml yaml = new Yaml();

    InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(nameFile);

    return yaml.load(inputStream);
  }
}