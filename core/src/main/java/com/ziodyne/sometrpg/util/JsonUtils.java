package com.ziodyne.sometrpg.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtils {
  private JsonUtils() { }

  private static final ObjectMapper MAPPER = new ObjectMapper();
  static {
    MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
  }

  public static <T> T readValue(InputStream stream, Class<T> clazz) throws IOException {

    return MAPPER.readValue(stream, clazz);
  }

  public static JsonNode readTree(InputStream stream) throws IOException {
    return MAPPER.readTree(stream);
  }
}
