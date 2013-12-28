package com.ziodyne.sometrpg.logic.models.cutscenes.actions;

import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.cutscenes.CutsceneDirector;
import com.ziodyne.sometrpg.logic.models.cutscenes.actions.CutsceneAction;

public class MapMovementAction implements CutsceneAction {
  public final Combatant actor;
  public final Tile destination;

  public MapMovementAction(Combatant actor, Tile destination) {
    this.actor = actor;
    this.destination = destination;
  }

  @Override
  public void receiveDirection(CutsceneDirector director) {
    director.direct(this);
  }
}
