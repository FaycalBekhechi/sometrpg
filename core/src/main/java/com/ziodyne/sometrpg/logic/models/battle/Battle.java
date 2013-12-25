package com.ziodyne.sometrpg.logic.models.battle;

import com.badlogic.gdx.math.GridPoint2;
import com.google.common.collect.ImmutableList;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.MapCombatResolver;
import com.ziodyne.sometrpg.logic.models.battle.conditions.WinCondition;
import com.ziodyne.sometrpg.logic.navigation.FloodFillRangeFinder;
import com.ziodyne.sometrpg.logic.navigation.RangeFinder;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Battle {
  private static final int TEMP_DEFAULT_MOVEMENT_RANGE = 7;

  private final RangeFinder movementRangeFinder = new FloodFillRangeFinder();
  private BattleMap map;
  private ImmutableList<Army> armies;
  private WinCondition condition;
  private int turnNumber;
  private MapCombatResolver combatResolver;

  public void setMap(BattleMap map) {
    this.map = map;
    this.combatResolver = new MapCombatResolver(map);
  }

  public void setArmies(List<Army> armies) {
    this.armies = ImmutableList.copyOf(armies);
  }

  public Set<GridPoint2> getMovableSquares(Combatant combatant) {
    GridPoint2 position = map.getCombatantPosition(combatant);
    return movementRangeFinder.computeRange(map, position, TEMP_DEFAULT_MOVEMENT_RANGE);
  }

  public void setCondition(WinCondition condition) {
    this.condition = condition;
  }

  public void setTurnNumber(int turnNumber) {
    this.turnNumber = turnNumber;
  }

  public void endTurn() {
    this.turnNumber++;
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
