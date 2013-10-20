package com.ziodyne.sometrpg.logic.util;

import com.ziodyne.sometrpg.logic.models.Map;

public class ModelTestUtils {
  private ModelTestUtils() { }

  public static Map createEmptyMap() {
    Map map = new Map(0, 0);

    return map;
  }
}
