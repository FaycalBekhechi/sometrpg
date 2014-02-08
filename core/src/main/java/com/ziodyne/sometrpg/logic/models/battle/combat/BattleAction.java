package com.ziodyne.sometrpg.logic.models.battle.combat;

/**
 * Represents a single, unit-affecting action on the battle. Encompasses various types of attack.
 */
public class BattleAction {
  private final Combatant attacker;
  private final Combatant defender;
  private final Attack attack;

  public BattleAction(Combatant attacker, Combatant defender, Attack attack) {
    this.attacker = attacker;
    this.defender = defender;
    this.attack = attack;
  }

  public Combatant getAttacker() {
    return attacker;
  }

  public Combatant getDefender() {
    return defender;
  }

  public Attack getAttack() {
    return attack;
  }
}
