package com.ziodyne.sometrpg.logic.navigation;

import com.badlogic.gdx.math.GridPoint2;
import com.google.common.collect.Lists;

import java.util.List;

public class Path {
  private List<GridPoint2> points;

  public static class Builder {
    private List<GridPoint2> points = Lists.newArrayList();

    public Builder addPoint(GridPoint2 point) {
      points.add(point);
      return this;
    }

    public Path build() {
      return new Path(points);
    }
  }

  public Path(List<GridPoint2> points) {
    this.points = points;
  }

  public List<GridPoint2> getPoints() {
    return points;
  }
}
