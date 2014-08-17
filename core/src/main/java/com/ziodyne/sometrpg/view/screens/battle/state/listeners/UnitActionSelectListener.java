package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import java.util.Set;
import javax.annotation.Nullable;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
  private OrthographicCamera camera;

  @Nullable
  private ActionMenu actionMenu;

  public UnitActionSelectListener(Engine engine, EntityFactory entityFactory, OrthographicCamera camera, float gridSize) {
    super(BattleState.SELECTING_UNIT_ACTION);

    this.engine = engine;
    this.entityFactory = entityFactory;
    this.gridSize = gridSize;
    this.camera = camera;
  }

  @Override
  public void onLeave(BattleContext context) {
    super.onLeave(context);

    if (actionMenu != null) {
      actionMenu.dispose();
    }

    Gdx.input.setInputProcessor(null);
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
      // Center the radial menu on the center of the selected unit's square.
      float x = context.selectedSquare.x * gridSize + (gridSize / 2);
      float y = context.selectedSquare.y * gridSize + (gridSize / 2);
      actionMenu = new ActionMenu(allowedActions, new Vector2(x, y), camera, engine, entityFactory);
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

      context.mapController.disable();
      Gdx.input.setInputProcessor(actionMenu);
    }
  }

}
