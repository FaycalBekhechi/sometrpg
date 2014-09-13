package com.ziodyne.sometrpg.view.controllers;

import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.view.entities.RenderedCombatant;
import com.ziodyne.sometrpg.view.widgets.ActionMenu;
import com.ziodyne.sometrpg.view.widgets.UnitInfoMenu;

import java.util.Collection;
import java.util.Set;

public interface MenuController {
  /**
   * Shows health bars under units.
   * @param combatants The {@link RenderedCombatant}s under which to display health
   */
  void enableHealthBars(Collection<RenderedCombatant> combatants);

  /**
   * Disables all health bars under units
   */
  void disableHealthBars();

  /**
   * Check if the health bars are currently enabled
   * @return True if health bars are enabled, and false otherwise
   */
  boolean healthBarsEnabled();

  /**
   * Spawn a unit info menu
   * @param combatant The combatant to inspect
   * @return The rendeed {@link UnitInfoMenu}
   */
  UnitInfoMenu showUnitInfo(Combatant combatant);

  /**
   * Spawn an action menu.
   * @param actions The available {@link CombatantAction}s that will appear in the menu
   * @param position The position of the menu
   * @return The rendered {@link ActionMenu}
   */
  ActionMenu showActionMenu(Set<CombatantAction> actions, Vector2 position);
}
