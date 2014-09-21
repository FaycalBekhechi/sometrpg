package com.ziodyne.sometrpg.logic.models.battle.combat;

public interface DeferredCombatResult {
  public boolean defenderWillDodge();
  public CombatResult execute();
}
