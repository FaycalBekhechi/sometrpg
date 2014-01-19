package com.ziodyne.sometrpg.logic.navigation;

import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.logic.util.MathUtils;

import java.util.Set;
import java.util.Stack;

public class FloodFillRangeFinder implements RangeFinder {
  @Override
  public Set<GridPoint2> computeRange(BattleMap map, GridPoint2 start, int maxDistance) {
    Set<GridPoint2> results = Sets.newHashSet();

    Stack<GridPoint2> fringe = new Stack<GridPoint2>();
    fringe.push(start);

    while (!fringe.isEmpty()) {
      GridPoint2 currentNode = fringe.pop();
      results.add(currentNode);

      for (GridPoint2 neighbor : MathUtils.getNeighbors(currentNode)) {
        boolean unseen = !results.contains(neighbor);
        boolean passable = map.isPassable(neighbor.x, neighbor.y);
        boolean inRange = MathUtils.manhattanDistance(start, neighbor) <= maxDistance;

        if (unseen && passable && inRange) {
          fringe.push(neighbor);
        }
      }
    }

    return results;
  }
}
