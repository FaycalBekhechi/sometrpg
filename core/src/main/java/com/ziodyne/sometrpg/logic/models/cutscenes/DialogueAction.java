package com.ziodyne.sometrpg.logic.models.cutscenes;

import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

public class DialogueAction implements CutsceneAction {
  public final Combatant actor;
  public final String line;

  public DialogueAction(Combatant actor, String line) {
    this.actor = actor;
    this.line = line;
  }
}
