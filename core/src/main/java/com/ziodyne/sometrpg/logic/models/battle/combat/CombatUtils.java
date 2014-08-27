package com.ziodyne.sometrpg.logic.models.battle.combat;

public class CombatUtils {
  private CombatUtils() { }

  public static CombatSummary previewBattle(BattleAction action) {
    Combatant attacker = action.getAttacker();
    Combatant defender = action.getDefender();
    Attack attack = action.getAttack();

    // TODO: Need a way to choose defender action from a higher level
    CombatantBattleResult attackerResult = new CombatantBattleResult(
      attacker,
      attack.computeDamage(attacker, defender),
      attack.computeHitChance(attacker, defender),
      attack.computeCritChance(attacker, defender)
    );

    CombatantBattleResult defenderResult = new CombatantBattleResult(
      defender,
      attack.computeDamage(defender, attacker),
      attack.computeHitChance(defender, attacker),
      attack.computeCritChance(defender, attacker)
    );

    return new CombatSummary(attackerResult, defenderResult);
  }
}
