package com.ziodyne.sometrpg.logic.navigation;

import com.badlogic.gdx.math.GridPoint2;
import com.google.common.collect.Lists;

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
      return new Path<T>(points);
    }
  }

  public Path(List<T> points) {
    this.points = points;
  }

  public List<T> getPoints() {
    return points;
  }
}
