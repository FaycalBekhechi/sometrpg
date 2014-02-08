package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import au.com.ds.ef.err.LogicViolationError;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

/**
 * A handler for the state where a the user must confirm a preview of an action.
 */
public class AttackConfirmationListener extends FlowListener<BattleContext> {
  public AttackConfirmationListener() {
    super(BattleState.AWAITING_ATTACK_CONFIRMATION);
  }

  @Override
  public void onLeave(BattleContext context) throws LogicViolationError {

  }

  @Override
  public void onEnter(BattleContext context) throws LogicViolationError {
    context.trigger(BattleEvent.ATTACK_CONFIRMED);
  }
}
