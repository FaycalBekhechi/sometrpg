package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import au.com.ds.ef.err.LogicViolationError;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

import java.util.Set;

public class UnitAttackingListener extends FlowListener<BattleContext> {
  public UnitAttackingListener() {
    super(BattleState.UNIT_ATTACKING);
  }

  @Override
  public void onLeave(BattleContext context) throws LogicViolationError {

  }

  @Override
  public void onEnter(BattleContext context) throws LogicViolationError {
    Combatant selectedCombatant = context.selectedCombatant;
    Set<CombatantAction> allowedActions = context.battle.getAvailableActions(selectedCombatant);
    if (allowedActions.isEmpty()) {
      context.trigger(BattleEvent.ACTIONS_EXHAUSTED);
    } else {
      context.trigger(BattleEvent.UNIT_ATTACKED);
    }
  }
}
