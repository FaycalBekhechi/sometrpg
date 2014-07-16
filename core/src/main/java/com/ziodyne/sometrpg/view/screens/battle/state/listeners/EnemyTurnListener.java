package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.ziodyne.sometrpg.logic.models.battle.TurnBased;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

public class EnemyTurnListener extends FlowListener<BattleContext> {

  private TurnBased turnBased;

  public EnemyTurnListener(TurnBased turnBased) {

    super(BattleState.ENEMY_TURN);
    this.turnBased = turnBased;
  }

  @Override
  public void onLeave(BattleContext context) {

  }

  @Override
  public void onEnter(BattleContext context) {
    // Just finish the turn immediately.
    turnBased.endTurn();
    context.safeTrigger(BattleEvent.ENEMY_TURN_FINISH);
  }
}
