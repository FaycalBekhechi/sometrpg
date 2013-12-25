package com.ziodyne.sometrpg.logic.navigation;

import com.badlogic.gdx.math.GridPoint2;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.Tile;

import java.util.Set;

public class BattleMapPathfindingStrategy implements PathfindingStrategy<GridPoint2> {
  private final BattleMap map;

  public BattleMapPathfindingStrategy(BattleMap map) {
    this.map = map;
  }

  @Override
  public double estimateCost(GridPoint2 node, GridPoint2 goal) {
    int dx = Math.abs(node.x - goal.x);
    int dy = Math.abs(node.y - goal.y);

    return dx + dy;
  }

  @Override
  public double distance(GridPoint2 start, GridPoint2 end) {
    Tile endTile = map.getTile(end.x, end.y);

    // Give this move a maximally high cost if the unit can't move there for any of the following reasons:
    //   - It's a tile not on the grid
    //   - The tile is occupied by another unit
    //   - The tile is marked as impassable
    if (endTile == null || endTile.isOccupied() || !endTile.isPassable()) {
      return Double.MAX_VALUE;
    }

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