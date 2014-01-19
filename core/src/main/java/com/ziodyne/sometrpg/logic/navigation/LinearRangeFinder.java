package com.ziodyne.sometrpg.logic.navigation;

import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.logic.util.MathUtils;

import java.util.HashSet;
import java.util.Set;

public class LinearRangeFinder implements RangeFinder {
  public static enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST
  }

  private final Direction direction;

  public LinearRangeFinder(Direction direction) {
    this.direction = direction;
  }

  @Override
  public Set<GridPoint2> computeRange(BattleMap map, GridPoint2 start, int maxDistance) {
    Set<GridPoint2> results = new HashSet<GridPoint2>();

    GridPoint2 currentPoint = start;
    for (int i = 1; i < maxDistance; i++) {
      if (!map.isPassable(currentPoint.x, currentPoint.y)) {
        break;
      }

      results.add(currentPoint);
      currentPoint = getNextInLine(currentPoint);
    }

    return results;
  }

  private GridPoint2 getNextInLine(GridPoint2 current) {
    switch (direction) {
      case NORTH:
        return MathUtils.getNorthNeighbor(current);
      case SOUTH:
        return MathUtils.getSouthNeighbor(current);
      case EAST:
        return MathUtils.getEastNeighbor(current);
      case WEST:
        return MathUtils.getWestNeighbor(current);
      default:
        return null;
    }
  }
}
