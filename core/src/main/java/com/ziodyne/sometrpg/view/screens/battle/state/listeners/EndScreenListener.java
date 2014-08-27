package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.ziodyne.sometrpg.util.Logged;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

public class EndScreenListener extends FlowListener<BattleContext> implements Logged {
  public EndScreenListener() {
    super(BattleState.END_SCREEN);
  }

  @Override
  public void onLeave(BattleContext context) {

  }

  @Override
  public void onEnter(BattleContext context) {
    if (context.won) {
      logDebug("Battle won!");
    } else {
      logDebug("Battle lost");
    }
  }
}
