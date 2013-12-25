package com.ziodyne.sometrpg.logic.models.battle.combat;

class CombatantBattleResult {
  private final Combatant combatant;
  private final int damage;
  private final int hitChancePct;
  private final int critChancePct;

  public CombatantBattleResult(Combatant combatant, int damage, int hitChancePct, int critChancePct) {
    this.combatant = combatant;
    this.damage = damage;
    this.hitChancePct = hitChancePct;
    this.critChancePct = critChancePct;
  }

  public Combatant getCombatant() {
    return combatant;
  }

  public int getDamage() {
    return damage;
  }

  public int getHitChancePct() {
    return hitChancePct;
  }

  public int getCritChancePct() {
    return critChancePct;
  }
}
