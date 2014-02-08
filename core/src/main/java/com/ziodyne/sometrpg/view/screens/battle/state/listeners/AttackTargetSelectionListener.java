package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import au.com.ds.ef.err.LogicViolationError;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

public class AttackTargetSelectionListener extends FlowListener<BattleContext> {

  public AttackTargetSelectionListener() {
    super(BattleState.SELECTING_ATTACK_TARGET);
  }

  @Override
  public void onLeave(BattleContext context) throws LogicViolationError {

  }

  @Override
  public void onEnter(BattleContext context) throws LogicViolationError {
    context.trigger(BattleEvent.TARGET_SELECTED);
  }
}
