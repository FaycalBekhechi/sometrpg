package com.ziodyne.sometrpg.logic.models.cutscenes;

import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.cutscenes.actions.CutsceneAction;

import java.util.List;

/**
 * A cutscene that allows for map movement as part of the script.
 */
public class MapCutscene extends ScriptedCutscene {
  private final BattleMapCutsceneDirector director;

  public MapCutscene(List<CutsceneAction> script, BattleMap map) {
    super(script);
    this.director = new BattleMapCutsceneDirector(map);
  }

  @Override
  public void nextAction(CutsceneAction nextAction) {
    nextAction.receiveDirection(director);
  }
}
