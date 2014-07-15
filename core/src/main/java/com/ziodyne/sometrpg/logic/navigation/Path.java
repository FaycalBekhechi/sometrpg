package com.ziodyne.sometrpg.logic.navigation;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class Path<T> {
  private final List<T> points;
  private final T start;

  public static class Builder<T> {
    private final List<T> points = Lists.newArrayList();

    private final T start;

    public Builder(T start) {

      this.start = start;
    }

    public Builder<T> addPoint(T point) {
      points.add(point);
      return this;
    }

    public Path<T> build() {
      Collections.reverse(points);
      return new Path<>(start, points);
    }
  }

  public T getStart() {

    return start;
  }

  public Path(T start, List<T> points) {

    this.start = start;
    this.points = points;
  }

  public List<T> getPoints() {
    return points;
  }

  @Override
  public String toString() {
    return points.toString();
  }
}
