package com.ziodyne.sometrpg.logic.navigation;

import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.util.GridPoint2;

import java.util.Set;

/**
 * A range finder computes movement or attack ranges on a map, from a starting square.
 *
 * The implementation may define exactly how the resulting squares are chosen.
 */
public interface RangeFinder {
  public Set<GridPoint2> computeRange(BattleMap map, GridPoint2 start, int maxDistance);
}
