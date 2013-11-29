package com.ziodyne.sometrpg.logic.models.battle.combat;

import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.UnitGrowth;
import com.ziodyne.sometrpg.logic.models.UnitStat;

import java.util.Set;

public class Combatant extends Unit {
  private int health;
  private boolean alive = true;

  public Combatant(Unit unit) {
    super(unit.getMaxStatSheet(), unit.getGrowths(), unit.getStatSheet(), unit.getName());
  }

  public Combatant(Set<UnitStat> maxStatSheet, UnitGrowth growths, Set<UnitStat> statSheet, String name) {
    super(maxStatSheet, growths, statSheet, name);
  }

  public void applyDamage(int amount) {
    // Overkill is the best kill, but keep things simple by bottoming out at 0.
    health = Math.max(health - amount, 0);
  }

  public boolean isAlive() {
    return alive;
  }
}
