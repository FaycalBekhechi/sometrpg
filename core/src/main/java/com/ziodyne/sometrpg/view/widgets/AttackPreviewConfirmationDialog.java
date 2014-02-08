package com.ziodyne.sometrpg.view.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.ziodyne.sometrpg.logic.models.battle.combat.BattleAction;

public class AttackPreviewConfirmationDialog extends Group {

  public static interface ConfirmedHandler {
    public void onConfirmed();
  }

  private final BattleAction action;
  private ConfirmedHandler confirmedHandler;

  public AttackPreviewConfirmationDialog(BattleAction action, Skin skin) {
    this.action = action;
    initialize(skin);
  }

  public void setConfirmedHandler(ConfirmedHandler confirmedHandler) {
    this.confirmedHandler = confirmedHandler;
  }

  private void initialize(Skin skin) {
    Actor confirmButton = new TextButton("Confirm", skin);
    confirmButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        if (confirmedHandler != null) {
          confirmedHandler.onConfirmed();
        }
      }
    });

    addActor(confirmButton);
  }
}
