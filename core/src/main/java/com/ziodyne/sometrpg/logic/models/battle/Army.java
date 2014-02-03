package com.ziodyne.sometrpg.logic.models.battle;

import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

import java.util.Set;

public class Army {
  public final Set<Combatant> units;
  public final String name;
  public final ArmyType type;

  public Army(Set<Combatant> units, String name, ArmyType type) {
    this.units = units;
    this.name = name;
    this.type = type;
  }

  public ArmyType getType() {
    return type;
  }

  public boolean contains(Combatant combatant) {
    return units.contains(combatant);
  }

  public Set<Combatant> getCombatants() {
    return units;
  }

  public Set<Combatant> getLivingCombatants() {
    Set<Combatant> living = Sets.newHashSet();
    for (Combatant combatant : getCombatants()) {
      if (combatant.isAlive()) {
        living.add(combatant);
      }
    }

    return living;
  }
}
