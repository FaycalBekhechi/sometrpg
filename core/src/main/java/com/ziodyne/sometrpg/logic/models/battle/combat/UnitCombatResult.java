package com.ziodyne.sometrpg.logic.models.battle.combat;

import com.ziodyne.sometrpg.logic.models.Unit;

public class UnitCombatResult {
  private final Unit unit;
  private final int damage;
  private final int hitChancePct;
  private final int critChancePct;

  public UnitCombatResult(Unit unit, int damage, int hitChancePct, int critChancePct) {
    this.unit = unit;
    this.damage = damage;
    this.hitChancePct = hitChancePct;
    this.critChancePct = critChancePct;
  }

  public Unit getUnit() {
    return unit;
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
