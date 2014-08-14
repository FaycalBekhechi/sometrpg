package com.ziodyne.sometrpg.view.widgets;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.logic.util.MathUtils;
import com.ziodyne.sometrpg.util.Logged;

import java.util.List;
import java.util.Set;

/**
 * A widget that renders the action menu as a LibGDX {@link Actor}
 */
public class ActionMenu extends Group implements Logged {
  private static final int RADIUS = 100;

  private ActionSelectedHandler selectedHandler;
  private final Set<CombatantAction> availableActions;
  private final Skin skin;

  public ActionMenu(Set<CombatantAction> availableActions, Skin skin) {
    this.availableActions = availableActions;
    this.skin = skin;

    initialize();
  }

  public void addSelectedListener(ActionSelectedHandler handler) {
    selectedHandler = handler;
  }

  private void initialize() {
    List<Vector2> samples = MathUtils.uniformSampleUnitCircle(availableActions.size());
    int i = 0;
    for (final CombatantAction action : availableActions) {
      Actor control = createControlForAction(action);

      Vector2 pos = samples.get(i);
      pos.scl(RADIUS);

      control.setPosition(pos.x, pos.y);

      control.addListener(new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          if (selectedHandler != null) {
            try {
              selectedHandler.handle(action);
            } catch (Exception e) {
              logError("Exception thrown in selection handler.", e);
            }
          }
        }
      });

      addActor(control);
      i++;
    }
  }

  private Actor createControlForAction(CombatantAction action) {
    return new TextButton(action.toString(), skin);
  }

}
