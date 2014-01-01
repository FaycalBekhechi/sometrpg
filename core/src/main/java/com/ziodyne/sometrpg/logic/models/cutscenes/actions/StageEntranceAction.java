package com.ziodyne.sometrpg.logic.models.cutscenes.actions;

import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.cutscenes.CutsceneDirector;

public class StageEntranceAction implements CutsceneAction {
  public static enum Type {
    STAGE_LEFT,
    STAGE_RIGHT
  }

  public final Character character;
  public final Type type;

  public StageEntranceAction(Character character, Type type) {
    this.character = character;
    this.type = type;
  }

  @Override
  public void receiveDirection(CutsceneDirector director) {
    director.direct(this);
  }
}
