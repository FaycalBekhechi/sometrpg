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

    Set<GridPoint2> finalResults = Sets.newHashSet();
    Set<Equivalence.Wrapper<GridPoint2>> results = floodFill(map, start, maxDistance);

    for (Equivalence.Wrapper<GridPoint2> gridPointWrapper : results) {
      GridPoint2 point = gridPointWrapper.get();
      finalResults.add(point);
    }

    return finalResults;
  }

  private static Set<Equivalence.Wrapper<GridPoint2>> floodFill(BattleMap map, GridPoint2 start, int maxDistance) {
    Set<Equivalence.Wrapper<GridPoint2>> results = Sets.newHashSet();

    Stack<GridPoint2> fringe = new Stack<GridPoint2>();
    fringe.push(start);

    while (!fringe.isEmpty()) {
      GridPoint2 currentNode = fringe.pop();
      results.add(wrapEquiv(currentNode));

      for (GridPoint2 neighbor : getNeighbors(currentNode)) {
        boolean unseen = !results.contains(wrapEquiv(neighbor));
        boolean passable = map.isPassable(neighbor.x, neighbor.y);
        boolean inRange = MathUtils.manhattanDistance(start, neighbor) <= maxDistance;

        if (unseen && passable && inRange) {
          fringe.push(neighbor);
        }
      }
    }

    return results;
  }

  private static Equivalence.Wrapper<GridPoint2> wrapEquiv(GridPoint2 point) {
    return MathUtils.GRID_POINT_EQUIV.wrap(point);
  }

  private static Set<GridPoint2> getNeighbors(GridPoint2 start) {
    return Sets.newHashSet(
      MathUtils.getEastNeighbor(start),
      MathUtils.getWestNeighbor(start),
      MathUtils.getNorthNeighbor(start),
      MathUtils.getSouthNeighbor(start)
    );
  }
}
