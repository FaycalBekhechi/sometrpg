package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import au.com.ds.ef.err.LogicViolationError;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

public class SelectingMoveLocation extends FlowListener<BattleContext> {
  private final BattleScreen battle;

  public SelectingMoveLocation(BattleScreen screen) {
    super(BattleState.SELECTING_MOVE_LOCATION);
    this.battle = screen;
  }

  @Override
  public void onLeave(BattleContext conext) throws LogicViolationError {
    battle.hideMoveRange();
  }

  @Override
  public void onEnter(BattleContext context) throws LogicViolationError {
    battle.showMoveRange(context.selectedCombatant);
  }
}
