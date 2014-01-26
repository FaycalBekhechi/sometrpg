package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import au.com.ds.ef.err.LogicViolationError;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

public class UnitActionSelectListener extends FlowListener<BattleContext> {
  public UnitActionSelectListener() {
    super(BattleState.SELECTING_UNIT_ACTION);
  }

  @Override
  public void onLeave(BattleContext conext) throws LogicViolationError {

  }

  @Override
  public void onEnter(BattleContext context) throws LogicViolationError {
    // For now, we always just select Move.
    context.trigger(BattleEvent.MOVE_ACTION_SELECTED);
  }
}
