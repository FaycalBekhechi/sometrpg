package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.google.common.collect.Iterables;
import com.ziodyne.sometrpg.logic.models.battle.SomeTRPGBattle;

public class Rout implements WinCondition {

  @Override
  public boolean isFulfilled(SomeTRPGBattle battle) {
    return Iterables.all(battle.getEnemyUnits(), ConditionUtils.IS_DEAD);
  }

  @Override
  public boolean isFailed(SomeTRPGBattle battle) {
    return Iterables.all(battle.getPlayerUnits(), ConditionUtils.IS_DEAD);
  }
}
