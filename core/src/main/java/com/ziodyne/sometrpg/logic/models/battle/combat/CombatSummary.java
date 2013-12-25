package com.ziodyne.sometrpg.logic.models.battle.combat;

public class CombatSummary {
  private final CombatantBattleResult attackerResult;
  private final CombatantBattleResult defenderResult;

  public CombatSummary(CombatantBattleResult attackerResult, CombatantBattleResult defenderResult) {
    this.attackerResult = attackerResult;
    this.defenderResult = defenderResult;
  }

  public CombatantBattleResult getAttackerResult() {
    return attackerResult;
  }

  public CombatantBattleResult getDefenderResult() {
    return defenderResult;
  }
}
