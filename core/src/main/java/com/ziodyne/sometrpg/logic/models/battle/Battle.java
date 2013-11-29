package com.ziodyne.sometrpg.logic.models.battle;

import com.google.common.collect.ImmutableList;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.conditions.WinCondition;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Battle {
  private BattleMap map;
  private ImmutableList<Army> armies;
  private WinCondition condition;
  private int turnNumber;

  public void setMap(BattleMap map) {
    this.map = map;
  }

  public void setArmies(List<Army> armies) {
    this.armies = ImmutableList.copyOf(armies);
  }

  public void setCondition(WinCondition condition) {
    this.condition = condition;
  }

  public void setTurnNumber(int turnNumber) {
    this.turnNumber = turnNumber;
  }

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

  public Set<Combatant> getPlayerUnits() {
    return getUnitsSafe(getArmyByType(ArmyType.PLAYER));
  }

  public Set<Combatant> getEnemyUnits() {
    return getUnitsSafe(getArmyByType(ArmyType.ENEMY));
  }

  public Set<Combatant> getNeutralUnits() {
    return getUnitsSafe(getArmyByType(ArmyType.NEUTRAL));
  }


  private Set<Combatant> getUnitsSafe(Army army) {
    return army == null ? new HashSet<Combatant>() : army.getUnits();
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
