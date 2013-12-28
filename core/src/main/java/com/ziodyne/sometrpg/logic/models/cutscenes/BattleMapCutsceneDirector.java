package com.ziodyne.sometrpg.logic.models.cutscenes;

import com.badlogic.gdx.math.GridPoint2;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;

public class BattleMapCutsceneDirector implements MapCutsceneDirector {
  private final BattleMap map;

  public BattleMapCutsceneDirector(BattleMap map) {
    this.map = map;
  }

  @Override
  public void direct(MapMovementAction mapMovementAction) {
    GridPoint2 actorPos = map.getCombatantPosition(mapMovementAction.actor);
    GridPoint2 destination = mapMovementAction.destination.getPosition();

    map.moveUnit(actorPos.x, actorPos.y, destination.x, destination.y);
  }

  @Override
  public void direct(DialogueAction dialogueAction) {

  }
}
