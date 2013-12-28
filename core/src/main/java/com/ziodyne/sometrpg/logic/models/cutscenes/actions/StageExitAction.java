package com.ziodyne.sometrpg.logic.models.cutscenes.actions;

import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.cutscenes.CutsceneDirector;

public class StageExitAction implements CutsceneAction {
  public static enum Type {
    STAGE_LEFT,
    STAGE_RIGHT
  }

  public final Unit unit;
  public final Type type;

  public StageExitAction(Unit unit, Type type) {
    this.unit = unit;
    this.type = type;
  }

  @Override
  public void receiveDirection(CutsceneDirector director) {
    director.direct(this);
  }
}
