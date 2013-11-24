package com.ziodyne.sometrpg.logic.models.battle;

import com.ziodyne.sometrpg.logic.models.Unit;

import java.util.Set;

public class Army {
  private final Set<Unit> units;
  private final String name;
  private final ArmyType type;

  public Army(Set<Unit> units, String name, ArmyType type) {
    this.units = units;
    this.name = name;
    this.type = type;
  }

  public ArmyType getType() {
    return type;
  }

  public Set<Unit> getUnits() {
    return units;
  }
}
