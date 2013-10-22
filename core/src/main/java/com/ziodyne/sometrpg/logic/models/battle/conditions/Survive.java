package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.ziodyne.sometrpg.logic.models.Map;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.battle.Battle;

import javax.annotation.Nullable;
import java.util.Set;

public class Survive implements WinCondition {
  private int turnGoal;

  public Survive(int turnGoal) {
    this.turnGoal = turnGoal;
  }

  @Override
  public boolean isFulfilled(Battle battle) {
    return battle.getTurnNumber() >= turnGoal;
  }

  @Override
  public boolean isFailed(Battle battle) {
    final Map map = battle.getMap();
    Set<Unit> players = battle.getPlayerUnits();
    Predicate<Unit> isAlive = new Predicate<Unit>() {
      @Override
      public boolean apply(@Nullable Unit unit) {
        return map.hasUnit(unit);
      }
    };

    return !Iterables.any(players, isAlive);
  }
}
