package com.ziodyne.sometrpg.logic.models.battle.combat;

public class CombatResult {
  private final int damageDone;
  private final boolean evaded;

  public CombatResult(int damageDone, boolean evaded) {

    this.damageDone = damageDone;
    this.evaded = evaded;
  }

  public int getDamageDone() {

    return damageDone;
  }

  public boolean wasEvaded() {

    return evaded;
  }
}
