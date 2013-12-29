package com.ziodyne.sometrpg.view.navigation;

import com.badlogic.gdx.math.GridPoint2;

public class PathSegment {
  public static enum Type {
    N,
    E,
    W,
    S,
    N2W,
    N2E,
    W2S,
    W2N,
    E2N,
    E2S,
    S2W,
    S2E,
    START,
    END
  }

  private final GridPoint2 point;
  private final Type type;

  public PathSegment(GridPoint2 point, Type type) {
    this.point = point;
    this.type = type;
  }

  public GridPoint2 getPoint() {
    return point;
  }

  public Type getType() {
    return type;
  }

  @Override
  public String toString() {
    return String.format("{ loc: (%d, %d), type: %s }", point.x, point.y, type);
  }
}
