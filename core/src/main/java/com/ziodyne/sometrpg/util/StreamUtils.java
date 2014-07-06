package com.ziodyne.sometrpg.util;

import java.util.stream.IntStream;

public class StreamUtils {
  public StreamUtils() { }

  public static IntStream iterateUpFrom(int base) {
    return IntStream.iterate(base, i -> i+1);
  }
}
