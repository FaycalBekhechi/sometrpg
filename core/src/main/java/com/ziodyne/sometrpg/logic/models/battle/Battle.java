package com.ziodyne.sometrpg.logic.models.battle;

import com.google.common.collect.ImmutableList;
import com.ziodyne.sometrpg.logic.models.BattleMap;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.battle.conditions.WinCondition;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class Battle {
  private BattleMap map;
  private ImmutableList<Army> armies;
  private WinCondition condition;
  private int turnNumber;

  public boolean playerWon() {
    return condition.isFulfilled(this);
  }

  public Army getCurrentTurnArmy() {
    return armies.get(turnNumber % armies.size());
  }

  public BattleMap getMap() {
    return map;
  }

  public int getTurnNumber() {
    return turnNumber;
  }

  public Set<Unit> getPlayerUnits() {
    return getUnitsSafe(getArmyByType(ArmyType.PLAYER));
  }

  public Set<Unit> getEnemyUnits() {
    return getUnitsSafe(getArmyByType(ArmyType.ENEMY));
  }

  private Set<Unit> getUnitsSafe(Army army) {
    return army == null ? new HashSet<Unit>() : army.getUnits();
  }

  @Nullable
  private Army getArmyByType(ArmyType type) {
    for (Army army : armies) {
      if (army.getType() == type) {
        return army;
      }
    }

    return null;
  }
}
