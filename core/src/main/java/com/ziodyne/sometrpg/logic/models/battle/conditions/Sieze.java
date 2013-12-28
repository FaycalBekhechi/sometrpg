package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.badlogic.gdx.math.GridPoint2;
import com.google.common.collect.Iterables;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
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
    Tile goalTile =  battle.getTile(new GridPoint2(goalTileX, goalTileY));
    Combatant occupyingUnit = goalTile.getCombatant();

    return occupyingUnit != null && battle.getPlayerUnits().contains(occupyingUnit);
  }

  @Override
  public boolean isFailed(Battle battle) {
    return Iterables.all(battle.getPlayerUnits(), ConditionUtils.IS_DEAD);
  }
}
