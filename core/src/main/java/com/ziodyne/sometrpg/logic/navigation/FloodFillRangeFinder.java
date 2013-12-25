package com.ziodyne.sometrpg.logic.navigation;

import com.badlogic.gdx.math.GridPoint2;
import com.google.common.base.Equivalence;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.util.MathUtils;

import java.util.Set;

public class FloodFillRangeFinder implements RangeFinder {
  @Override
  public Set<GridPoint2> computeRange(BattleMap map, GridPoint2 start, int maxDistance) {
    Set<Equivalence.Wrapper<GridPoint2>> results = Sets.newHashSet();

    flood(map, start, maxDistance, 0, results);

    Set<GridPoint2> finalResults = Sets.newHashSet();
    for (Equivalence.Wrapper<GridPoint2> point2Wrapper : results) {
      GridPoint2 point = point2Wrapper.get();
      finalResults.add(point);
    }

    return finalResults;
  }

  private void flood(BattleMap map, GridPoint2 node, int maxDistance, int currentDistance, Set<Equivalence.Wrapper<GridPoint2>> accumulator) {
    if (currentDistance > maxDistance) {
      return;
    }

    Equivalence.Wrapper<GridPoint2> wrappedNode = MathUtils.GRID_POINT_EQUIV.wrap(node);
    if (accumulator.contains(wrappedNode)) {
      return;
    }

    if (!map.isPassable(node.x, node.y)) {
      return;
    }

    accumulator.add(wrappedNode);

    int nextDist = currentDistance + 1;
    flood(map, MathUtils.getEastNeighbor(node), maxDistance, nextDist, accumulator);
    flood(map, MathUtils.getWestNeighbor(node), maxDistance, nextDist, accumulator);
    flood(map, MathUtils.getNorthNeighbor(node), maxDistance, nextDist, accumulator);
    flood(map, MathUtils.getSouthNeighbor(node), maxDistance, nextDist, accumulator);
  }
}
