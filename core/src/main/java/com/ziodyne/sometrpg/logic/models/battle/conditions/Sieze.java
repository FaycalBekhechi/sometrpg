package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.google.common.collect.Iterables;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.SomeTRPGBattle;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.exceptions.GameLogicException;
import com.ziodyne.sometrpg.logic.util.GridPoint2;

public class Sieze implements WinCondition {
  private int goalTileX;
  private int goalTileY;

  public Sieze(int goalTileX, int goalTileY) {

    this.goalTileX = goalTileX;
    this.goalTileY = goalTileY;
  }

  @Override
  public boolean isFulfilled(SomeTRPGBattle battle) {
    Tile goalTile =  battle.getTile(new GridPoint2(goalTileX, goalTileY));
    Combatant occupyingUnit = goalTile.getCombatant();

    return occupyingUnit != null && battle.getPlayerUnits().contains(occupyingUnit);
  }

  @Override
  public boolean isFailed(SomeTRPGBattle battle) {
    return Iterables.all(battle.getPlayerUnits(), ConditionUtils.IS_DEAD);
  }
}
