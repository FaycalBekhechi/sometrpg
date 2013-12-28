package com.ziodyne.sometrpg.logic.models.cutscenes;

public interface MapCutsceneDirector extends CutsceneDirector {
  public void direct(MapMovementAction mapMovementAction);
}
