package com.ziodyne.sometrpg.logic.models.battle.combat;

import com.badlogic.gdx.math.GridPoint2;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;

public class MapCombatResolver implements CombatResolver {
  private final BattleMap map;

  public MapCombatResolver(BattleMap map) {
    this.map = map;
  }

  @Override
  public boolean isValid(BattleAction action) {
    GridPoint2 attackerPos = map.getCombatantPosition(action.getAttacker());
    if (attackerPos == null) {
      return false;
    }

    GridPoint2 defenderPos = map.getCombatantPosition(action.getDefender());
    if (defenderPos == null) {
      return false;
    }

    return false;
  }

  @Override
  public CombatSummary preview(BattleAction action) throws InvalidBattleActionException {
    validate(action);
    return CombatUtils.previewBattle(action);
  }

  @Override
  public void execute(BattleAction action) throws InvalidBattleActionException {
    validate(action);
  }

  private void validate(BattleAction action) throws InvalidBattleActionException {
    if (!isValid(action)) {
      throw new InvalidBattleActionException();
    }
  }
}
