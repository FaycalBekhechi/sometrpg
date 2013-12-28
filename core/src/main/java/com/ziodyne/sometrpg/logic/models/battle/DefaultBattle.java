package com.ziodyne.sometrpg.logic.models.battle;

import com.badlogic.gdx.math.GridPoint2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.battle.combat.Attack;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.MapCombatResolver;
import com.ziodyne.sometrpg.logic.models.battle.conditions.WinCondition;
import com.ziodyne.sometrpg.logic.models.exceptions.GameLogicException;
import com.ziodyne.sometrpg.logic.navigation.FloodFillRangeFinder;
import com.ziodyne.sometrpg.logic.navigation.RangeFinder;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultBattle implements Battle, TileNavigable, TurnBased {
  private final RangeFinder movementRangeFinder = new FloodFillRangeFinder();
  private final BattleMap map;
  private final ImmutableList<Army> armies;
  private final WinCondition condition;
  private MapCombatResolver combatResolver;
  private Set<Combatant> actedThisTurn = Sets.newHashSet();
  private Set<Combatant> movedThisTurn = Sets.newHashSet();

  private int turnNumber;

  public DefaultBattle(BattleMap map, List<Army> armies, WinCondition condition) {
    this.map = map;
    this.armies = ImmutableList.copyOf(armies);
    this.condition = condition;
    this.combatResolver = new MapCombatResolver(map);
  }

  @Override
  public boolean tileExists(GridPoint2 point) {
    return map.tileExists(point.x, point.y);
  }

  @Override
  public Tile getTile(GridPoint2 point) {
    return map.getTile(point.x, point.y);
  }

  @Override
  public void moveCombatant(Combatant combatant, Tile destination) {
    validateCombatantStatus(combatant);
    validateCombatantTurn(combatant);

    GridPoint2 currentPos = map.getCombatantPosition(combatant);
    if (currentPos == null) {
      throw new GameLogicException("Cannot move combatant not on the map.");
    }

    GridPoint2 destPos = destination.getPosition();
    map.moveUnit(currentPos.x, currentPos.y, destPos.x, destPos.y);

    recordMovement(combatant);
  }

  @Override
  public void executeAttack(Combatant attacker, Attack attack, Combatant defender) {
    if (actedThisTurn.contains(attacker)) {
      throw new GameLogicException("Cannot perform two actions on the same turn.");
    }

    if (!defender.isAlive()) {
      throw new GameLogicException("Cannot attack a dead combatant.");
    }

    // Attacking also consumes your one move for a turn
    recordMovement(attacker);
    recordAction(attacker);
  }

  @Override
  public Set<Tile> getMovableTiles(Combatant combatant) {
    GridPoint2 position = map.getCombatantPosition(combatant);
    if (position == null) {
      throw new GameLogicException("Cannot get movement range for combatant that is not on the map.");
    }

    Set<GridPoint2> movablePoints = movementRangeFinder.computeRange(map, position, combatant.getMovementRange());

    Set<Tile> movableTiles = Sets.newHashSet();
    for (GridPoint2 point : movablePoints) {
      Tile tile = map.getTile(point.x, point.y);
      movableTiles.add(tile);
    }

    return movableTiles;
  }

  @Override
  public boolean isWon() {
    return condition.isFulfilled(this);
  }

  @Override
  public boolean isLost() {
    return condition.isFailed(this);
  }

  @Override
  public Army getCurrentTurnArmy() {
    return armies.get(turnNumber % armies.size());
  }

  @Override
  public int getTurnNumber() {
    return turnNumber;
  }

  @Override
  public Set<Combatant> getPlayerUnits() {
    return getUnitsSafe(getArmyByType(ArmyType.PLAYER));
  }

  @Override
  public Set<Combatant> getEnemyUnits() {
    return getUnitsSafe(getArmyByType(ArmyType.ENEMY));
  }

  @Override
  public Set<Combatant> getNeutralUnits() {
    return getUnitsSafe(getArmyByType(ArmyType.NEUTRAL));
  }

  @Override
  public void endTurn() {
    actedThisTurn = Sets.newHashSet();
    movedThisTurn = Sets.newHashSet();
    turnNumber++;
  }

  private void recordAction(Combatant combatant) {
    actedThisTurn.add(combatant);
  }

  private void recordMovement(Combatant combatant) {
    movedThisTurn.add(combatant);
  }

  private boolean isTurnComplete() {
    Army currentArmy = getCurrentTurnArmy();
    return actedThisTurn.containsAll(currentArmy.units);
  }


  private Set<Combatant> getUnitsSafe(Army army) {
    return army == null ? new HashSet<Combatant>() : army.getUnits();
  }

  private void validateCombatantTurn(Combatant combatant) {
    Army currentArmy = getCurrentTurnArmy();
    if (!currentArmy.contains(combatant)) {
      throw new GameLogicException("It is not this combatant's turn.");
    }
  }

  private static void validateCombatantStatus(Combatant combatant) {
    if (!combatant.isAlive()) {
      throw new GameLogicException("Combatant is dead.");
    }
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
