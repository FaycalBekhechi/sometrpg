package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.*;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
import com.ziodyne.sometrpg.logic.models.battle.SomeTRPGBattle;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ProtectUnits implements WinCondition {
  private final Set<Character> unitsToProtect;

  public ProtectUnits(Character unitToProtect) {
    this(Sets.newHashSet(unitToProtect));
  }

  public ProtectUnits(Set<Character> unitsToProtect) {
    this.unitsToProtect = unitsToProtect;
  }

  private Collection<Combatant> resolveCombatants(Battle battle) {
    return unitsToProtect.stream()
      .map(battle::getCombatantForCharacter)
      .collect(Collectors.toList());
  }

  @Override
  public boolean isFulfilled(SomeTRPGBattle battle) {

    return Iterables.all(resolveCombatants(battle), ConditionUtils.IS_ALIVE);
  }

  @Override
  public boolean isFailed(SomeTRPGBattle battle){
    return Iterables.any(resolveCombatants(battle), ConditionUtils.IS_DEAD);
  }
}
