package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

/**
 * Logic for entering and exiting the state where a unit is moving
 */
public class UnitMoving extends FlowListener<BattleContext> {
  private final BattleScreen battleScreen;

  public UnitMoving(BattleScreen screen) {
    super(BattleState.UNIT_MOVING);
    this.battleScreen = screen;
  }

  @Override
  public void onLeave(BattleContext context) {

  }

  @Override
  public void onEnter(BattleContext context) {
    battleScreen.moveCombatant(context.selectedCombatant, context.movementDestination);
    context.selectedSquare = context.movementDestination;

    // Unit movement is instant for now
    context.safeTrigger(BattleEvent.UNIT_MOVED);
  }
}
