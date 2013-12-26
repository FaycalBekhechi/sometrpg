package com.ziodyne.sometrpg.logic.models.battle;

import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

import java.util.Set;

public class Army {
  private final Set<Combatant> units;
  private final String name;
  private final ArmyType type;

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

  public Set<Combatant> getUnits() {
    return units;
  }
}
