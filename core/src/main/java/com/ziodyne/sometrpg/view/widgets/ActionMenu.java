package com.ziodyne.sometrpg.view.widgets;

import java.util.Set;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.util.Logged;
import com.ziodyne.sometrpg.view.entities.EntityFactory;


/**
 * A widget that renders the action menu as a LibGDX {@link Actor}
 */
public class ActionMenu implements Disposable, Logged {
  private static final int RADIUS = 100;

  private ActionSelectedHandler selectedHandler;
  private final Set<CombatantAction> availableActions;
  private final Vector2 position;
  private final Entity menuWedge;
  private final Engine engine;

  public ActionMenu(Set<CombatantAction> availableActions, Vector2 position, Engine engine, EntityFactory entityFactory) {
    this.availableActions = availableActions;
    this.position = position;
    this.menuWedge = entityFactory.createRadialMenuWedge(position);
    this.engine = engine;

    initialize();
  }

  public void addSelectedListener(ActionSelectedHandler handler) {
    selectedHandler = handler;
  }

  private void initialize() {
    engine.addEntity(menuWedge);
  }

  @Override
  public void dispose() {
    engine.removeEntity(menuWedge);
  }
}
