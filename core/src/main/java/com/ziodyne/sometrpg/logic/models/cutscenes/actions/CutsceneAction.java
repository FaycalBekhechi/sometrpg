package com.ziodyne.sometrpg.logic.models.cutscenes.actions;

import com.ziodyne.sometrpg.logic.models.cutscenes.CutsceneDirector;

/**
 * Just an interface to unify various actions in a script.
 */
public interface CutsceneAction {
  void receiveDirection(CutsceneDirector director);
}
