package com.ziodyne.sometrpg.logic.models.battle.combat;

import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.UnitGrowth;
import com.ziodyne.sometrpg.logic.models.UnitStat;
import com.ziodyne.sometrpg.logic.util.UnitUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Set;

public class Combatant {
  private final Unit unit;
  private int health;
  private boolean alive = true;

  public Combatant(Unit unit) {
    this.unit = unit;
    this.alive = true;
    this.health = UnitUtils.getMaxHealth(unit);
  }

  public void applyDamage(int amount) {
    // Overkill is the best kill, but keep things simple by bottoming out at 0.
    health = Math.max(health - amount, 0);
  }

  public long getUnitId() {
    return unit.getId();
  }

  public boolean isAlive() {
    return alive;
  }

  public Unit getUnit() {
    return unit;
  }

  public int getHealth() {
    return health;
  }

  public int getMovementRange() {
    return unit.getMovementRange();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (!(obj instanceof Combatant)) {
      return false;
    }

    return new EqualsBuilder()
            .append(unit, ((Combatant) obj).getUnit())
            .build();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(unit)
            .build();
  }
}
