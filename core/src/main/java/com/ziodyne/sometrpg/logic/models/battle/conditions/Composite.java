package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.ziodyne.sometrpg.logic.models.battle.SomeTRPGBattle;

import javax.annotation.Nullable;
import java.util.Set;

public abstract class Composite implements WinCondition {
  protected Set<WinCondition> conditions;

  protected Composite(Set<WinCondition> conditions) {
    this.conditions = conditions;
  }

  /** Any of the given conditions must be met in order to win. */
  public static class Some extends Composite {
    public Some(Set<WinCondition> conditions) {
      super(conditions);
    }

    @Override
    public boolean isFulfilled(final SomeTRPGBattle battle) {
      Predicate<WinCondition> fulfilled = new Predicate<WinCondition>() {
        @Override
        public boolean apply(@Nullable WinCondition condition) {
          return condition != null && condition.isFulfilled(battle);
        }
      };

      return Iterables.any(conditions, fulfilled);
    }

    @Override
    public boolean isFailed(final SomeTRPGBattle battle) {
      Predicate<WinCondition> failed = new Predicate<WinCondition>() {
        @Override
        public boolean apply(@Nullable WinCondition condition) {
          return condition != null && condition.isFailed(battle);
        }
      };

      return Iterables.any(conditions, failed);
    }
  }

  /** All of the given conditions must be met in order to win. */
  public static class All extends Composite {
    public All(Set<WinCondition> conditions) {
      super(conditions);
    }

    @Override
    public boolean isFulfilled(final SomeTRPGBattle battle) {
      Predicate<WinCondition> fulfilled = new Predicate<WinCondition>() {
        @Override
        public boolean apply(@Nullable WinCondition condition) {
          return condition != null && condition.isFulfilled(battle);
        }
      };

      return Iterables.all(conditions, fulfilled);
    }

    @Override
    public boolean isFailed(final SomeTRPGBattle battle) {
      Predicate<WinCondition> failed = new Predicate<WinCondition>() {
        @Override
        public boolean apply(@Nullable WinCondition condition) {
          return condition != null && condition.isFailed(battle);
        }
      };

      return Iterables.all(conditions, failed);
    }
  }
}
