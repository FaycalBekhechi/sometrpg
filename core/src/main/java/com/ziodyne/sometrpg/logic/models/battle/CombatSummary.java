package com.ziodyne.sometrpg.logic.models.battle;

public class CombatSummary {
  private final UnitCombatResult attackerResult;
  private final UnitCombatResult defenderResult;

  public CombatSummary(UnitCombatResult attackerResult, UnitCombatResult defenderResult) {
    this.attackerResult = attackerResult;
    this.defenderResult = defenderResult;
  }

  public UnitCombatResult getAttackerResult() {
    return attackerResult;
  }

  public UnitCombatResult getDefenderResult() {
    return defenderResult;
  }
}
