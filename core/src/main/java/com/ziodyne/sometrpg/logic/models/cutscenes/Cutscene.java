package com.ziodyne.sometrpg.logic.models.cutscenes;

public interface Cutscene {
  /**
   * Perform the next action in the script, or nothing if there are no more.
   */
  public void nextAction();
}
