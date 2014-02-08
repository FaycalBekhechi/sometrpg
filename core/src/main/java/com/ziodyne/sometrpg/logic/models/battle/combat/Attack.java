package com.ziodyne.sometrpg.logic.models.battle.combat;

public interface Attack {
  public int computeDamage(Combatant attacker, Combatant defender);
  public int computeHitChance(Combatant attacker, Combatant defender);
  public int computeCritChance(Combatant attacker, Combatant defender);
  public int getRange();
}
