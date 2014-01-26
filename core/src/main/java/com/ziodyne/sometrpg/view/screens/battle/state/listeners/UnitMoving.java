package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import au.com.ds.ef.err.LogicViolationError;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

public class UnitMoving extends FlowListener<BattleContext> {
  private final BattleScreen battleScreen;

  public UnitMoving(BattleScreen screen) {
    super(BattleState.UNIT_MOVING);
    this.battleScreen = screen;
  }

  @Override
  public void onLeave(BattleContext context) throws LogicViolationError {

  }

  @Override
  public void onEnter(BattleContext context) throws LogicViolationError {
    battleScreen.moveCombatant(context.selectedCombatant, context.movementDestination);

    // Unit movement is instant for now
    context.trigger(BattleEvent.UNIT_MOVED);
  }
}
