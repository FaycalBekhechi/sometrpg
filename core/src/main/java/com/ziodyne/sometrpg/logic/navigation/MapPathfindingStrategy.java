package com.ziodyne.sometrpg.logic.navigation;

import com.badlogic.gdx.math.GridPoint2;
import com.google.common.collect.Sets;

import java.util.Set;

public class MapPathfindingStrategy implements PathfindingStrategy<GridPoint2> {
  @Override
  public double estimateCost(GridPoint2 node, GridPoint2 goal) {
    int dx = Math.abs(node.x - goal.x);
    int dy = Math.abs(node.y - goal.y);

    return dx + dy;
  }

  @Override
  public double distance(GridPoint2 start, GridPoint2 end) {
    return Math.sqrt(Math.pow(start.x - end.x, 2) + Math.pow(start.y - end.y, 2));
  }

  @Override
  public Set<GridPoint2> getNeighbors(GridPoint2 node) {
    int x = node.x;
    int y = node.y;

    return Sets.newHashSet(
      new GridPoint2(x+1, y),
      new GridPoint2(x-1, y),
      new GridPoint2(x, y+1),
      new GridPoint2(x, y-1)
    );
  }

  @Override
  public boolean isGoal(GridPoint2 node, GridPoint2 goal) {
    return node.x == goal.x &&
           node.y == goal.y;
  }
}
