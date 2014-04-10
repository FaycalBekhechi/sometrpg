package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ziodyne.sometrpg.logging.GdxLogger;
import com.ziodyne.sometrpg.logging.Logger;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.InputStealingFlowListener;
import com.ziodyne.sometrpg.view.widgets.ActionMenu;
import com.ziodyne.sometrpg.view.widgets.ActionSelectedHandler;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Logic for entering and exiting the state where the player is selecting an action for a unit.
 */
public class UnitActionSelectListener extends InputStealingFlowListener<BattleContext> {
  private static final Logger LOG = new GdxLogger(UnitActionSelectListener.class);
  private Skin skin;
  private OrthographicCamera camera;
  private Stage stage;

  @Nullable
  private ActionMenu actionMenu;

  public UnitActionSelectListener(Skin skin, OrthographicCamera camera, Stage stage) {
    super(BattleState.SELECTING_UNIT_ACTION);

    this.skin = skin;
    this.camera = camera;
    this.stage = stage;
  }

  @Override
  public void onLeave(BattleContext context) {
    super.onLeave(context);

    if (actionMenu != null) {
      actionMenu.clear();
    }

    context.mapController.enable();
  }

  @Override
  public void onEnter(final BattleContext context) {
    super.onEnter(context);

    Combatant selectedCombatant = context.selectedCombatant;
    Set<CombatantAction> allowedActions = context.battle.getAvailableActions(selectedCombatant);
    if (allowedActions.isEmpty()) {
      LOG.log("Unit actions exhausted.");
      context.safeTrigger(BattleEvent.ACTIONS_EXHAUSTED);
    } else {

      actionMenu = new ActionMenu(allowedActions, skin);

      Vector3 screenSpaceSelectionCoords = new Vector3(context.selectedSquare.x+1, context.selectedSquare.y, 0);
      camera.project(screenSpaceSelectionCoords);

      actionMenu.setX(screenSpaceSelectionCoords.x);
      actionMenu.setY(screenSpaceSelectionCoords.y);

      actionMenu.addSelectedListener(new ActionSelectedHandler() {
        @Override
        public void handle(CombatantAction selectedAction) throws Exception {
          switch (selectedAction) {
            case ATTACK:
              context.safeTrigger(BattleEvent.ATTACK_ACTION_SELECTED);
              break;
            case MOVE:
              context.safeTrigger(BattleEvent.MOVE_ACTION_SELECTED);
              break;
            default:
              throw new IllegalArgumentException("Combatant action " + selectedAction + " not mapped to event.");
          }
        }
      });

      stage.addActor(actionMenu);
      Gdx.input.setInputProcessor(stage);
      context.mapController.disable();
    }
  }

}
