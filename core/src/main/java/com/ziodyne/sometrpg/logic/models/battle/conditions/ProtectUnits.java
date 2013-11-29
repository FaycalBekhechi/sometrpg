package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

import java.util.Set;

public class ProtectUnits implements WinCondition {
  private final Set<Combatant> unitsToProtect;
  private final int turnCount;

  public ProtectUnits(Combatant unitToProtect, int turnCount) {
    this(Sets.newHashSet(unitToProtect), turnCount);
  }

  public ProtectUnits(Set<Combatant> unitsToProtect, int turnCount) {
    this.unitsToProtect = unitsToProtect;
    this.turnCount = turnCount;
  }

  @Override
  public boolean isFulfilled(Battle battle) {
    return Iterables.all(unitsToProtect, ConditionUtils.IS_ALIVE);
  }

  @Override
  public boolean isFailed(Battle battle) {
    return Iterables.any(unitsToProtect, ConditionUtils.IS_DEAD);
  }
}
