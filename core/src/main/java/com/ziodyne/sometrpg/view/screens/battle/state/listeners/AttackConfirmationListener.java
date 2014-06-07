package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
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
  private final float gridSize;
  private AttackPreviewConfirmationDialog confirmationDialog;

  public AttackConfirmationListener(Skin skin, Stage stage, OrthographicCamera camera, float gridSize) {
    super(BattleState.AWAITING_ATTACK_CONFIRMATION);
    this.skin = skin;
    this.stage = stage;
    this.camera = camera;
    this.gridSize = gridSize;
  }

  @Override
  public void onLeave(BattleContext context) {
    super.onLeave(context);

    if (confirmationDialog != null) {
      confirmationDialog.clear();
    }
  }

  @Override
  public void onEnter(final BattleContext context) {
    super.onEnter(context);
    Optional<BattleAction> action = context.getAction();
    Preconditions.checkState(action.isPresent(), "Attack confirmation requested with no attack to perform.");

    confirmationDialog = new AttackPreviewConfirmationDialog(action.get(), skin);
    confirmationDialog.setX((context.selectedSquare.x+1) * gridSize);
    confirmationDialog.setY(context.selectedSquare.y * gridSize);
    confirmationDialog.setConfirmedHandler(new AttackPreviewConfirmationDialog.ConfirmedHandler() {
      @Override
      public void onConfirmed() {

        context.safeTrigger(BattleEvent.ATTACK_CONFIRMED);
      }
    });

    stage.addActor(confirmationDialog);
    Gdx.input.setInputProcessor(stage);
  }
}
