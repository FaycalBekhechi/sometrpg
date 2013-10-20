package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.ziodyne.sometrpg.logic.models.battle.Battle;

public interface WinCondition {
  public boolean isFulfilled(Battle battle);
}
