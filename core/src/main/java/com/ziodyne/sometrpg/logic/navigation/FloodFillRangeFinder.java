package com.ziodyne.sometrpg.logic.navigation;

import com.badlogic.gdx.math.GridPoint2;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.util.MathUtils;

import java.util.HashSet;
import java.util.Set;

public class FloodFillRangeFinder implements RangeFinder {
  @Override
  public Set<GridPoint2> computeRange(BattleMap map, GridPoint2 start, int maxDistance) {
    Set<GridPoint2> results = new HashSet<GridPoint2>();

    flood(map, start, maxDistance, 0, results);

    return results;
  }

  private void flood(BattleMap map, GridPoint2 node, int maxDistance, int currentDistance, Set<GridPoint2> accumulator) {
    if (currentDistance > maxDistance) {
      return;
    }

    if (!map.isPassable(node.x, node.y)) {
      return;
    }

    accumulator.add(node);

    int nextDist = currentDistance + 1;
    flood(map, MathUtils.getEastNeighbor(node), maxDistance, nextDist, accumulator);
    flood(map, MathUtils.getWestNeighbor(node), maxDistance, nextDist, accumulator);
    flood(map, MathUtils.getNorthNeighbor(node), maxDistance, nextDist, accumulator);
    flood(map, MathUtils.getSouthNeighbor(node), maxDistance, nextDist, accumulator);
  }
}
