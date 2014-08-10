package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import java.util.Set;
import javax.annotation.Nullable;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.util.Logged;
import com.ziodyne.sometrpg.view.entities.EntityFactory;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.InputStealingFlowListener;
import com.ziodyne.sometrpg.view.widgets.ActionMenu;

/**
 * Logic for entering and exiting the state where the player is selecting an action for a unit.
 */
public class UnitActionSelectListener extends InputStealingFlowListener<BattleContext> implements Logged {
  private Skin skin;
  private Stage stage;
  private Viewport viewport;
  private float gridSize;
  private Engine engine;
  private EntityFactory entityFactory;

  @Nullable
  private ActionMenu actionMenu;

  public UnitActionSelectListener(Engine engine, EntityFactory entityFactory, float gridSize) {
    super(BattleState.SELECTING_UNIT_ACTION);

    this.engine = engine;
    this.entityFactory = entityFactory;
    this.gridSize = gridSize;
  }

  @Override
  public void onLeave(BattleContext context) {
    super.onLeave(context);

    if (actionMenu != null) {
      actionMenu.dispose();
    }

    context.mapController.enable();
  }

  @Override
  public void onEnter(final BattleContext context) {
    super.onEnter(context);

    Combatant selectedCombatant = context.selectedCombatant;
    Set<CombatantAction> allowedActions = context.battle.getAvailableActions(selectedCombatant);
    if (allowedActions.size() == 1 && allowedActions.contains(CombatantAction.INFO)) {
      logDebug("Unit actions exhausted.");
      context.safeTrigger(BattleEvent.ACTIONS_EXHAUSTED);
    } else {

      actionMenu = new ActionMenu(allowedActions, new Vector2(context.selectedSquare.x * gridSize, context.selectedSquare.y * gridSize), engine, entityFactory);
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
          default:
            throw new IllegalArgumentException("Combatant action " + selectedAction + " not mapped to event.");
        }
      });

      //Gdx.input.setInputProcessor(stage);
      context.mapController.disable();
    }
  }

}
