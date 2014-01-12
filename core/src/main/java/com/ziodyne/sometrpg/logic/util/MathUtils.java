package com.ziodyne.sometrpg.logic.util;

import com.badlogic.gdx.math.GridPoint2;
import com.google.common.base.Equivalence;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

public class MathUtils {

  private MathUtils() { }


  /**
   * An {@link com.google.common.base.Equivalence} implementation for {@link com.badlogic.gdx.math.GridPoint2}
   * that defines two {@link com.badlogic.gdx.math.GridPoint2} equal if their x and y components are equal.
   *
   * This really should be part of LibGDX, but they dropped the ball.
   */
  public static Equivalence<GridPoint2> GRID_POINT_EQUIV = new Equivalence<GridPoint2>() {
    @Override
    protected boolean doEquivalent(GridPoint2 a, GridPoint2 b) {
      if (a == null && b == null) {
        return true;
      }

      if (a == null || b == null) {
        return false;
      }

      return new EqualsBuilder()
              .append(a.x, b.x)
              .append(a.y, b.y)
              .build();
    }

    @Override
    protected int doHash(GridPoint2 gridPoint2) {
      return new HashCodeBuilder(17, 37)
              .append(gridPoint2.x)
              .append(gridPoint2.y)
              .build();
    }
  };

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
