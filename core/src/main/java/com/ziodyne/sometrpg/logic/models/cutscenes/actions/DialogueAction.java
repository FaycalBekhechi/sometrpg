package com.ziodyne.sometrpg.logic.models.cutscenes.actions;

import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.cutscenes.CutsceneDirector;

public class DialogueAction implements CutsceneAction {
  public final Combatant actor;
  public final String line;

  public DialogueAction(Combatant actor, String line) {
    this.actor = actor;
    this.line = line;
  }

  @Override
  public void receiveDirection(CutsceneDirector director) {
    director.direct(this);
  }
}
