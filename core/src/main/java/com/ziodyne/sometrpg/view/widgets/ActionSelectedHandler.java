package com.ziodyne.sometrpg.view.widgets;

import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;

/**
 * A function that can handle the selection of an action for a combatant from the action menu.
 */
public interface ActionSelectedHandler {
  public void handle(CombatantAction selectedAction);
}
