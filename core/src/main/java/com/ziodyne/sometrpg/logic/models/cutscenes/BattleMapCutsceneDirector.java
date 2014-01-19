package com.ziodyne.sometrpg.logic.models.cutscenes;

import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.cutscenes.actions.DialogueAction;
import com.ziodyne.sometrpg.logic.models.cutscenes.actions.MapMovementAction;
import com.ziodyne.sometrpg.logic.models.cutscenes.actions.StageEntranceAction;
import com.ziodyne.sometrpg.logic.models.cutscenes.actions.StageExitAction;
import com.ziodyne.sometrpg.logic.util.GridPoint2;

public class BattleMapCutsceneDirector implements CutsceneDirector {
  private final BattleMap map;

  public BattleMapCutsceneDirector(BattleMap map) {
    this.map = map;
  }

  @Override
  public void direct(MapMovementAction mapMovementAction) {
    GridPoint2 actorPos = map.getCombatantPosition(mapMovementAction.actor);
    GridPoint2 destination = mapMovementAction.destination.getPosition();

    if (actorPos != null) {
      map.moveUnit(actorPos.x, actorPos.y, destination.x, destination.y);
    }
  }

  @Override
  public void direct(DialogueAction dialogueAction) {

  }

  @Override
  public void direct(StageEntranceAction stageEntranceAction) {

  }

  @Override
  public void direct(StageExitAction stageExitAction) {

  }
}
