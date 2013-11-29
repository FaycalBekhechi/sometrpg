package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.google.common.base.Predicate;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

import javax.annotation.Nullable;

public class ConditionUtils {
  private ConditionUtils() { }

  public static Predicate<Combatant> IS_DEAD = new Predicate<Combatant>() {
    @Override
    public boolean apply(@Nullable Combatant input) {
      return input == null || !input.isAlive();
    }
  };

  public static Predicate<Combatant> IS_ALIVE = new Predicate<Combatant>() {
    @Override
    public boolean apply(@Nullable Combatant input) {
      return input != null && input.isAlive();
    }
  };
}
