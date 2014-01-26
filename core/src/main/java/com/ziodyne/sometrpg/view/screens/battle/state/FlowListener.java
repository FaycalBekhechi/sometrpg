package com.ziodyne.sometrpg.view.screens.battle.state;

import au.com.ds.ef.EasyFlow;

public interface FlowListener {
  public void bind(EasyFlow<BattleContext> flow);
}
