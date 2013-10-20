package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.ziodyne.sometrpg.logic.models.battle.Battle;

public class Survive implements WinCondition {
  private int turnGoal;

  public Survive(int turnGoal) {
    this.turnGoal = turnGoal;
  }

  @Override
  public boolean isFulfilled(Battle battle) {
    return battle.getTurnNumber() >= turnGoal;
  }
}
