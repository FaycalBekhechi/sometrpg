package com.ziodyne.sometrpg.view.screens.battle.eventhandlers;

import com.google.common.eventbus.Subscribe;
import com.ziodyne.sometrpg.events.UnitMoved;
import com.ziodyne.sometrpg.view.screens.battle.UnitMover;

public class UnitMoveHandler {
  private UnitMover combatantMover;

  public UnitMoveHandler(UnitMover combatantMover) {

    this.combatantMover = combatantMover;
  }

  @Subscribe
  public void handle(UnitMoved unitMoved) {

    combatantMover.moveCombatant(unitMoved.getCombatant(), unitMoved.getPath());
  }

}
