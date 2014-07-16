package com.ziodyne.sometrpg.logic.navigation;

import java.util.Set;
import java.util.stream.Collectors;

import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.util.GridPoint2;

public class AttackRangeFinder implements RangeFinder {

  @Override
  public Set<GridPoint2> computeRange(BattleMap map, GridPoint2 start, int maxDistance) {
    return map.getNeighborsInRadius(start, maxDistance).stream()
      .collect(Collectors.toSet());
  }
}
