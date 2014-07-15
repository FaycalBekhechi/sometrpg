package com.ziodyne.sometrpg.logic.navigation;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class Path<T> {
  private List<T> points;

  public static class Builder<T> {
    private List<T> points = Lists.newArrayList();

    public Builder<T> addPoint(T point) {
      points.add(point);
      return this;
    }

    public Path<T> build() {
      Collections.reverse(points);
      return new Path<>(points);
    }
  }

  public Path(List<T> points) {
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
