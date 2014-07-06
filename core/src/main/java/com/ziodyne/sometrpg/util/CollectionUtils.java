package com.ziodyne.sometrpg.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionUtils {

  private CollectionUtils() { }

  public static <K, U> Map<U, K> indexBy(Collection<K> collection, Function<K, U> mapper) {
    return collection.stream()
      .collect(Collectors.toMap(mapper, Function.identity()));
  }

  public static <K, U> Map<U, List<K>> groupBy(Collection<K> collection, Function<K, U> mapper) {
    return collection.stream()
      .collect(Collectors.groupingBy(mapper));
  }
}
