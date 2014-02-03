package com.ziodyne.sometrpg.view.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.ziodyne.sometrpg.logging.GdxLogger;
import com.ziodyne.sometrpg.logging.Logger;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;

import java.util.Set;

/**
 * A widget that renders the action menu as a LibGDX {@link Actor}
 */
public class ActionMenu extends Group {
  private static final Logger LOG = new GdxLogger(ActionMenu.class);

  private ActionSelectedHandler selectedHandler;
  private Set<CombatantAction> availableActions;
  private Skin skin;

  public ActionMenu(Set<CombatantAction> availableActions, Skin skin) {
    this.availableActions = availableActions;
    this.skin = skin;

    initialize();
  }

  public void addSelectedListener(ActionSelectedHandler handler) {
    selectedHandler = handler;
  }

  private void initialize() {
    float dy = 0;
    for (final CombatantAction action : availableActions) {
      Actor control = createControlForAction(action);
      control.setY(dy);

      control.addListener(new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          if (selectedHandler != null) {
            try {
              selectedHandler.handle(action);
            } catch (Exception e) {
              LOG.error("Exception thrown in selection handler.", e);
            }
          }
        }
      });

      addActor(control);
      dy += 30;
    }
  }

  private Actor createControlForAction(CombatantAction action) {
    return new TextButton(action.toString(), skin);
  }
}
