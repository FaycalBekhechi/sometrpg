package com.ziodyne.sometrpg.logic.models.battle.combat;

public interface CombatResolver {
  public boolean isValid(BattleAction action);
  public CombatSummary preview(BattleAction action);
  public CombatResult execute(BattleAction action);
}
