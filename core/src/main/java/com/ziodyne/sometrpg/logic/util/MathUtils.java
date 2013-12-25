package com.ziodyne.sometrpg.logic.util;

import com.badlogic.gdx.math.GridPoint2;

public class MathUtils {

  private MathUtils() { }

  public static GridPoint2 getNorthNeighbor(GridPoint2 point) {
    return new GridPoint2(point.x, point.y - 1);
  }

  public static GridPoint2 getSouthNeighbor(GridPoint2 point) {
    return new GridPoint2(point.x, point.y + 1);
  }

  public static GridPoint2 getWestNeighbor(GridPoint2 point) {
    return new GridPoint2(point.x - 1, point.y);
  }

  public static GridPoint2 getEastNeighbor(GridPoint2 point) {
    return new GridPoint2(point.x + 1, point.y);
  }
}
