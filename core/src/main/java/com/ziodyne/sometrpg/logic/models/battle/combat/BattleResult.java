package com.ziodyne.sometrpg.logic.models.battle.combat;

import java.util.Optional;

public class BattleResult {
  private final CombatResult initalAttack;
  private final Optional<CombatResult> counterAttack;


  public BattleResult(CombatResult initalAttack, CombatResult counterAttack) {
    this.initalAttack = initalAttack;
    this.counterAttack = Optional.ofNullable(counterAttack);
  }

  public BattleResult(CombatResult initialAttack) {
    this.initalAttack = initialAttack;
    this.counterAttack = Optional.empty();
  }

  public boolean didCounter() {
    return counterAttack.isPresent();
  }

  public CombatResult getInitalAttack() {
    return initalAttack;
  }

  public Optional<CombatResult> getCounterAttack() {
    return counterAttack;
  }
}
