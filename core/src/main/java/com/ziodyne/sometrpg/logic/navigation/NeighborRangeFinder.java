package com.ziodyne.sometrpg.logic.navigation;

import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.util.GridPoint2;

import java.util.Set;
import java.util.stream.Collectors;

public class NeighborRangeFinder implements RangeFinder {

  @Override
  public Set<GridPoint2> computeRange(BattleMap map, final GridPoint2 start, final int maxDistance) {
    return map.getNeighborsInRadius(start, maxDistance).stream()
      .filter((point) -> map.pathExists(start, point))
      .collect(Collectors.toSet());
  }
}
