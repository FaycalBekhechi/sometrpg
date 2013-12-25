package com.ziodyne.sometrpg.logic.models.battle.combat;

public class WeaponAttack implements Attack {
  @Override
  public int computeHitChance(Combatant attacker, Combatant defender) {
    return 0;
  }

  @Override
  public int computeCritChance(Combatant attacker, Combatant defender) {
    return 0;
  }

  @Override
  public int computeDamage(Combatant attacker, Combatant defender) {
    return 0;
  }
}
