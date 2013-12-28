package com.ziodyne.sometrpg.logic.models.cutscenes;

import com.ziodyne.sometrpg.logic.models.battle.BattleMap;

import java.util.List;

public class MapCutscene extends AbstractCutscene {
  private final BattleMapCutsceneDirector director;

  public MapCutscene(List<CutsceneAction> script, BattleMap map) {
    super(script);
    this.director = new BattleMapCutsceneDirector(map);
  }

  @Override
  public void nextAction(CutsceneAction nextAction) {

  }
}
