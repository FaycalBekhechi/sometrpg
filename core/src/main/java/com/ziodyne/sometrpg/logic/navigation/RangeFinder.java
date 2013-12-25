package com.ziodyne.sometrpg.logic.navigation;

import com.badlogic.gdx.math.GridPoint2;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;

import java.util.Set;

public interface RangeFinder {
  public Set<GridPoint2> computeRange(BattleMap map, GridPoint2 start, int maxDistance);
}
