package com.ziodyne.sometrpg.logic.models.battle;

import com.ziodyne.sometrpg.logic.models.Map;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.battle.conditions.WinCondition;

import java.util.Set;

public class Battle {
  private Map map;
  private Set<Unit> playerUnits;
  private Set<Unit> enemyUnits;
  private WinCondition condition;
  private int turnNumber;

  public boolean playerWon() {
    return condition.isFulfilled(this);
  }

  public Map getMap() {
    return map;
  }

  public int getTurnNumber() {
    return turnNumber;
  }

  public Set<Unit> getPlayerUnits() {
    return playerUnits;
  }

  public Set<Unit> getEnemyUnits() {
    return enemyUnits;
  }
}
