package com.ziodyne.sometrpg.logic.navigation;

import com.badlogic.gdx.math.GridPoint2;
import com.google.common.base.Equivalence;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.util.MathUtils;

import java.util.Set;
import java.util.Stack;

public class FloodFillRangeFinder implements RangeFinder {
  @Override
  public Set<GridPoint2> computeRange(BattleMap map, GridPoint2 start, int maxDistance) {
    Set<Equivalence.Wrapper<GridPoint2>> results = Sets.newHashSet();

    Stack<GridPoint2> nodes = new Stack<GridPoint2>();
    nodes.push(start);

    while (!nodes.isEmpty()) {
      GridPoint2 currentNode = nodes.pop();
      results.add(MathUtils.GRID_POINT_EQUIV.wrap(currentNode));

      GridPoint2 west = MathUtils.getWestNeighbor(currentNode);
      Equivalence.Wrapper<GridPoint2> wrappedWest = MathUtils.GRID_POINT_EQUIV.wrap(west);
      if (!results.contains(wrappedWest) && map.isPassable(west.x, west.y) && MathUtils.manhattanDistance(start, west) < maxDistance) {
        nodes.push(west);
      }

      GridPoint2 east = MathUtils.getEastNeighbor(currentNode);
      Equivalence.Wrapper<GridPoint2> wrappedEast = MathUtils.GRID_POINT_EQUIV.wrap(east);
      if (!results.contains(wrappedEast) && map.isPassable(east.x, east.y) && MathUtils.manhattanDistance(start, east) < maxDistance) {
        nodes.push(east);
      }

      GridPoint2 north = MathUtils.getNorthNeighbor(currentNode);
      Equivalence.Wrapper<GridPoint2> wrappedNorth = MathUtils.GRID_POINT_EQUIV.wrap(north);
      if (!results.contains(wrappedNorth) && map.isPassable(north.x, north.y) && MathUtils.manhattanDistance(start, north) < maxDistance) {
        nodes.push(north);
      }

      GridPoint2 south = MathUtils.getSouthNeighbor(currentNode);
      Equivalence.Wrapper<GridPoint2> wrappedSouth = MathUtils.GRID_POINT_EQUIV.wrap(south);
      if (!results.contains(wrappedSouth) && map.isPassable(south.x, south.y) && MathUtils.manhattanDistance(start, south) < maxDistance) {
        nodes.push(south);
      }
    }

    Set<GridPoint2> finalResults = Sets.newHashSet();
    for (Equivalence.Wrapper<GridPoint2> point2Wrapper : results) {
      GridPoint2 point = point2Wrapper.get();
      finalResults.add(point);
    }

    return finalResults;
  }
}
