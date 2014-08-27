package com.ziodyne.sometrpg.events;

import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

public class CombatantActed {
  private final Combatant combatant;

  public CombatantActed(Combatant combatant) {
    this.combatant = combatant;
  }

  public Combatant getCombatant() {
    return combatant;
  }
}
