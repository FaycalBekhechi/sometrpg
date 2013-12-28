package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.google.common.collect.Iterables;
import com.ziodyne.sometrpg.logic.models.battle.SomeTRPGBattle;

public class Survive implements WinCondition {
  private int turnGoal;

  public Survive(int turnGoal) {
    this.turnGoal = turnGoal;
  }

  @Override
  public boolean isFulfilled(SomeTRPGBattle battle) {
    return battle.getTurnNumber() >= turnGoal;
  }

  @Override
  public boolean isFailed(SomeTRPGBattle battle) {
    return Iterables.all(battle.getPlayerUnits(), ConditionUtils.IS_DEAD);
  }
}
