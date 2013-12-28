package com.ziodyne.sometrpg.logic.models.cutscenes;

import com.ziodyne.sometrpg.logic.models.cutscenes.actions.CutsceneAction;

import java.util.List;

/**
 * Default abstract implementation of {@link Cutscene} that works from a script.
 */
public abstract class ScriptedCutscene implements Cutscene {
  private List<CutsceneAction> script;
  private int actionIndex;

  public ScriptedCutscene(List<CutsceneAction> script) {
    this.script = script;
  }

  @Override
  public void nextAction() {
    if (actionIndex <= script.size()) {
      nextAction(script.get(actionIndex));
      actionIndex++;
    }
  }

  protected abstract void nextAction(CutsceneAction action);
}
