package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.exceptions.GameLogicException;

public class Sieze implements WinCondition {
  private int goalTileX;
  private int goalTileY;

  public Sieze(int goalTileX, int goalTileY, BattleMap map) {
    if (!map.tileExists(goalTileX, goalTileY)) {
      throw new GameLogicException("Cannot create a Sieze win condition for a non-existant tile.");
    }

    this.goalTileX = goalTileX;
    this.goalTileY = goalTileY;
  }

  @Override
  public boolean isFulfilled(Battle battle) {
    BattleMap map = battle.getMap();
    Tile goalTile =  map.getTile(goalTileX, goalTileY);
    Combatant occupyingUnit = goalTile.getOccupyingUnit();

    return occupyingUnit != null && battle.getPlayerUnits().contains(occupyingUnit);
  }

  @Override
  public boolean isFailed(Battle battle) {
    return Iterables.all(battle.getPlayerUnits(), ConditionUtils.IS_DEAD);
  }
}
