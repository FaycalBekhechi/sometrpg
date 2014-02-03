package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import au.com.ds.ef.err.LogicViolationError;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;
import com.ziodyne.sometrpg.view.widgets.ActionMenu;
import com.ziodyne.sometrpg.view.widgets.ActionSelectedHandler;

import java.util.Set;

/**
 * Logic for entering and exiting the state where the player is selecting an action for a unit.
 */
public class UnitActionSelectListener extends FlowListener<BattleContext> {
  private Skin skin;
  private OrthographicCamera camera;
  private Stage stage;
  private ActionMenu actionMenu;
  private InputProcessor previousInputProcessor;

  public UnitActionSelectListener(Skin skin, OrthographicCamera camera, Stage stage) {
    super(BattleState.SELECTING_UNIT_ACTION);

    this.skin = skin;
    this.camera = camera;
    this.stage = stage;
  }

  @Override
  public void onLeave(BattleContext context) throws LogicViolationError {
    actionMenu.clear();
    context.mapController.enable();;
    if (previousInputProcessor != null) {
      Gdx.input.setInputProcessor(previousInputProcessor);
    }
  }

  @Override
  public void onEnter(final BattleContext context) throws LogicViolationError {
    Combatant selectedCombatant = context.selectedCombatant;
    Set<CombatantAction> allowedActions = context.battle.getAvailableActions(selectedCombatant);

    actionMenu = new ActionMenu(allowedActions, skin);

    Vector3 screenSpaceSelectionCoords = new Vector3(context.selectedSquare.x+1, context.selectedSquare.y, 0);
    camera.project(screenSpaceSelectionCoords);

    actionMenu.setX(screenSpaceSelectionCoords.x);
    actionMenu.setY(screenSpaceSelectionCoords.y);

    actionMenu.addSelectedListener(new ActionSelectedHandler() {
      @Override
      public void handle(CombatantAction selectedAction) throws Exception {
        // For now, we always just select Move.
        context.trigger(BattleEvent.MOVE_ACTION_SELECTED);
      }
    });

    stage.addActor(actionMenu);
    previousInputProcessor = Gdx.input.getInputProcessor();
    Gdx.input.setInputProcessor(stage);
    context.mapController.disable();
  }
}
