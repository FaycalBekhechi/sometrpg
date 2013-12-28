package com.ziodyne.sometrpg.logic.models.cutscenes;

import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

public class MapMovementAction implements CutsceneAction {
  public final Combatant actor;
  public final Tile destination;

  public MapMovementAction(Combatant actor, Tile destination) {
    this.actor = actor;
    this.destination = destination;
  }
}
