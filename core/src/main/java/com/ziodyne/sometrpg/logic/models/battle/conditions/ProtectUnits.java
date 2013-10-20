package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.Map;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.battle.Battle;

import javax.annotation.Nullable;
import java.util.Set;

public class ProtectUnits implements WinCondition {
  private Set<Unit> unitsToProtect;

  public ProtectUnits(Unit unitToProtect) {
    this(Sets.newHashSet(unitToProtect));
  }

  public ProtectUnits(Set<Unit> unitsToProtect) {
    this.unitsToProtect = unitsToProtect;
  }

  @Override
  public boolean isFulfilled(Battle battle) {
    final Map map = battle.getMap();

    Predicate<Unit> isAlive = new Predicate<Unit>() {
      @Override
      public boolean apply(@Nullable Unit unit) {
        return map.hasUnit(unit);
      }
    };

    return Iterables.all(unitsToProtect, isAlive);
  }
}
