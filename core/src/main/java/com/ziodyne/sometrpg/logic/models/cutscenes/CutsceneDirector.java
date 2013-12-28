package com.ziodyne.sometrpg.logic.models.cutscenes;

import com.ziodyne.sometrpg.logic.models.cutscenes.actions.DialogueAction;
import com.ziodyne.sometrpg.logic.models.cutscenes.actions.MapMovementAction;
import com.ziodyne.sometrpg.logic.models.cutscenes.actions.StageEntranceAction;
import com.ziodyne.sometrpg.logic.models.cutscenes.actions.StageExitAction;

/**
 * A visitor for {@link com.ziodyne.sometrpg.logic.models.cutscenes.actions.CutsceneAction}s.
 */
public interface CutsceneDirector {
  /**
   * Perform a dialogue script action for a cutscene.
   *
   * @param dialogueAction the dialogue action to perform
   */
  public void direct(DialogueAction dialogueAction);

  /**
   * Perform a map movement action for a cutscene.
   *
   * @param mapMovementAction the map movement action to perform
   */
  public void direct(MapMovementAction mapMovementAction);

  /**
   * Perform a stage entrance
   *
   * @param stageEntranceAction the stage entrance to perform
   */
  public void direct(StageEntranceAction stageEntranceAction);

  /**
   * Perform a stage exit
   *
   * @param stageExitAction the stage exit to perform
   */
  public void direct(StageExitAction stageExitAction);
}
