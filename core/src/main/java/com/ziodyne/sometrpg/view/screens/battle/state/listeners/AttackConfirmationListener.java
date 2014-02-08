package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import au.com.ds.ef.err.LogicViolationError;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.common.base.Optional;
import com.ziodyne.sometrpg.logic.models.battle.combat.BattleAction;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.InputStealingFlowListener;
import com.ziodyne.sometrpg.view.widgets.AttackPreviewConfirmationDialog;

/**
 * A handler for the state where a the user must confirm a preview of an action.
 */
public class AttackConfirmationListener extends InputStealingFlowListener<BattleContext> {
  private final Skin skin;
  private final Stage stage;
  private final OrthographicCamera camera;
  private AttackPreviewConfirmationDialog confirmationDialog;

  public AttackConfirmationListener(Skin skin, Stage stage, OrthographicCamera camera) {
    super(BattleState.AWAITING_ATTACK_CONFIRMATION);
    this.skin = skin;
    this.stage = stage;
    this.camera = camera;
  }

  @Override
  public void onLeave(BattleContext context) throws LogicViolationError {
    super.onLeave(context);

    if (confirmationDialog != null) {
      confirmationDialog.clear();
    }
  }

  @Override
  public void onEnter(final BattleContext context) throws LogicViolationError {
    super.onEnter(context);
    Optional<BattleAction> action = context.getAction();
    if (!action.isPresent()) {
      throw new LogicViolationError("Attack confirmation requested with no attack to perform.");
    }

    confirmationDialog = new AttackPreviewConfirmationDialog(action.get(), skin);

    Vector3 screenSpaceSelectionCoords = new Vector3(context.selectedSquare.x+1, context.selectedSquare.y, 0);
    camera.project(screenSpaceSelectionCoords);

    confirmationDialog.setX(screenSpaceSelectionCoords.x);
    confirmationDialog.setY(screenSpaceSelectionCoords.y);

    confirmationDialog.setConfirmedHandler(new AttackPreviewConfirmationDialog.ConfirmedHandler() {
      @Override
      public void onConfirmed() {
        try {
          context.trigger(BattleEvent.ATTACK_CONFIRMED);
        } catch (LogicViolationError ignored) { }
      }
    });

    stage.addActor(confirmationDialog);
    Gdx.input.setInputProcessor(stage);
  }
}
