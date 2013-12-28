package com.ziodyne.sometrpg.logic.models.cutscenes;

import java.util.List;

public abstract class AbstractCutscene implements Cutscene {
  private List<CutsceneAction> script;
  private int actionIndex;

  public AbstractCutscene(List<CutsceneAction> script) {
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
