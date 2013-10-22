package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.ziodyne.sometrpg.logic.models.battle.Battle;

public class Rout implements WinCondition {
  @Override
  public boolean isFulfilled(Battle battle) {
    return battle.getEnemyUnits().isEmpty();
  }

  @Override
  public boolean isFailed(Battle battle) {
    return false;
  }
}
