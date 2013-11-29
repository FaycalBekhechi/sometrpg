package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

import javax.annotation.Nullable;
import java.util.Set;

public class Survive implements WinCondition {
  private int turnGoal;

  public Survive(int turnGoal) {
    this.turnGoal = turnGoal;
  }

  @Override
  public boolean isFulfilled(Battle battle) {
    return battle.getTurnNumber() >= turnGoal;
  }

  @Override
  public boolean isFailed(Battle battle) {
    final BattleMap map = battle.getMap();
    Set<Combatant> players = battle.getPlayerUnits();
    Predicate<Combatant> isAlive = new Predicate<Combatant>() {
      @Override
      public boolean apply(Combatant unit) {
        return unit.isAlive();
      }
    };

    return !Iterables.any(players, isAlive);
  }
}
