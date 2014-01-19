package com.ziodyne.sometrpg.logic.util;

import com.google.common.collect.Sets;

import java.util.Set;

public class MathUtils {

  private MathUtils() { }

  public static Set<GridPoint2> getNeighbors(GridPoint2 start) {
    return Sets.newHashSet(
            MathUtils.getEastNeighbor(start),
            MathUtils.getWestNeighbor(start),
            MathUtils.getNorthNeighbor(start),
            MathUtils.getSouthNeighbor(start)
    );
  }

  public static int manhattanDistance(GridPoint2 start, GridPoint2 end) {
    return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
  }

  public static GridPoint2 getNorthNeighbor(GridPoint2 point) {
    return new GridPoint2(point.x, point.y + 1);
  }

  public static GridPoint2 getSouthNeighbor(GridPoint2 point) {
    return new GridPoint2(point.x, point.y - 1);
  }

  public static GridPoint2 getWestNeighbor(GridPoint2 point) {
    return new GridPoint2(point.x - 1, point.y);
  }

  public static GridPoint2 getEastNeighbor(GridPoint2 point) {
    return new GridPoint2(point.x + 1, point.y);
  }
}
