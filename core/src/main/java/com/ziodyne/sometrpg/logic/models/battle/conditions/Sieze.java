package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.ziodyne.sometrpg.logic.models.Map;
import com.ziodyne.sometrpg.logic.models.Tile;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.battle.Battle;

public class Sieze implements WinCondition {
  private int goalTileX;
  private int goalTileY;

  public Sieze(int goalTileX, int goalTileY) {
    this.goalTileX = goalTileX;
    this.goalTileY = goalTileY;
  }

  @Override
  public boolean isFulfilled(Battle battle) {
    Map map = battle.getMap();
    Tile goalTile =  map.getTile(goalTileX, goalTileY);
    Unit occupyingUnit = goalTile.getOccupyingUnit();

    return occupyingUnit != null && battle.getPlayerUnits().contains(occupyingUnit);
  }
}
