package com.ziodyne.sometrpg.logic.models.battle.combat;

public class CombatResult {
  private final int damageDone;
  private final boolean evaded;
  private final boolean targetKilled;

  public CombatResult(int damageDone, boolean evaded, boolean targetKilled) {
    this.damageDone = damageDone;
    this.evaded = evaded;
    this.targetKilled = targetKilled;
  }

  public boolean wasTargetKilled() {
    return targetKilled;
  }

  public int getDamageDone() {

    return damageDone;
  }

  public boolean wasEvaded() {

    return evaded;
  }
}
