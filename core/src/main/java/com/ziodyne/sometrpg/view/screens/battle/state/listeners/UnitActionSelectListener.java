package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Lists;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.util.Logged;
import com.ziodyne.sometrpg.view.AnimationType;
import com.ziodyne.sometrpg.view.components.BattleUnit;
import com.ziodyne.sometrpg.view.controllers.MenuController;
import com.ziodyne.sometrpg.view.entities.RenderedCombatant;
import com.ziodyne.sometrpg.view.input.InputHandlerStack;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;
import com.ziodyne.sometrpg.view.widgets.ActionMenu;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Logic for entering and exiting the state where the player is selecting an action for a unit.
 */
public class UnitActionSelectListener extends FlowListener<BattleContext> implements Logged {
  private final InputHandlerStack handlers;
  private final MenuController menuController;
  private final BattleScreen battleScreen;
  private final float gridSize;

  @Nullable
  private ActionMenu actionMenu;

  public UnitActionSelectListener(MenuController menuController, InputHandlerStack handlers, BattleScreen battleScreen,
                                  float gridSize) {
    super(BattleState.SELECTING_UNIT_ACTION);

    this.battleScreen = battleScreen;
    this.gridSize = gridSize;
    this.handlers = handlers;
    this.menuController = menuController;
  }

  @Override
  public void onLeave(BattleContext context) {

    if (actionMenu != null) {
      actionMenu.dispose();
    }

    handlers.pop();
    context.mapController.enable();
    context.battleView.enableCameraControl();
  }

  @Override
  public void onEnter(final BattleContext context) {

    Combatant selectedCombatant = context.selectedCombatant;
    Set<CombatantAction> allowedActions = context.battle.getAvailableActions(selectedCombatant);
    if (allowedActions.size() == 1 && allowedActions.contains(CombatantAction.INFO)) {
      logDebug("Unit actions exhausted.");
      context.safeTrigger(BattleEvent.ACTIONS_EXHAUSTED);
    } else {
      // Center the radial menu on the center of the selected unit's square.
      float x = context.selectedSquare.x * gridSize + (gridSize / 2);
      float y = context.selectedSquare.y * gridSize + (gridSize / 2);
      actionMenu = menuController.showActionMenu(allowedActions, new Vector2(x, y));
      actionMenu.addSelectedListener(selectedAction -> {
        switch (selectedAction) {
          case ATTACK:
            context.safeTrigger(BattleEvent.ATTACK_ACTION_SELECTED);
            break;
          case MOVE:
            context.safeTrigger(BattleEvent.MOVE_ACTION_SELECTED);
            break;
          case INFO:
            context.safeTrigger(BattleEvent.INFO_ACTION_SELECTED);
            break;
          case END_TURN:
            context.battle.endTurn();
            context.safeTrigger(BattleEvent.END_TURN);
            break;
          default:
            throw new IllegalArgumentException("Combatant action " + selectedAction + " not mapped to event.");
        }
      });

      context.battleView.disableCameraControl();
      context.mapController.disable();
      handlers.push(actionMenu);
    }
  }

}
