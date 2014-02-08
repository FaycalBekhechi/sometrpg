package com.ziodyne.sometrpg.logic.models.battle.combat;

public class WeaponAttack implements Attack {
  @Override
  public int getRange() {
    return 1;
  }

  @Override
  public int computeHitChance(Combatant attacker, Combatant defender) {
    return 100;
  }

  @Override
  public int computeCritChance(Combatant attacker, Combatant defender) {
    return 0;
  }

  @Override
  public int computeDamage(Combatant attacker, Combatant defender) {
    return 1000;
  }
}
