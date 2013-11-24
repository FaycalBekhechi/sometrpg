package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.ziodyne.sometrpg.logic.models.BattleMap;
import com.ziodyne.sometrpg.logic.models.Tile;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
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
    Unit occupyingUnit = goalTile.getOccupyingUnit();

    return occupyingUnit != null && battle.getPlayerUnits().contains(occupyingUnit);
  }

  @Override
  public boolean isFailed(Battle battle) {
    return false;
  }
}
