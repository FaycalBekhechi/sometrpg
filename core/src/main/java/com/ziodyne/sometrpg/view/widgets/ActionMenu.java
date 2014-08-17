package com.ziodyne.sometrpg.view.widgets;

import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.util.Logged;
import com.ziodyne.sometrpg.view.entities.EntityFactory;

public class ActionMenu extends InputAdapter implements Disposable, Logged {
  private final RadialMenu radialMenu;

  public ActionMenu(Set<CombatantAction> availableActions, Vector2 position, OrthographicCamera camera, Engine engine, EntityFactory entityFactory) {
    radialMenu = new RadialMenu(engine, entityFactory, position, camera, availableActions.stream().map(this::toMenuItem).collect(
      Collectors.toSet()));
    radialMenu.render();
  }

  private RadialMenu.Item toMenuItem(CombatantAction action) {
    return new RadialMenu.Item(action.name(), action.name());
  }

  public void addSelectedListener(ActionSelectedHandler handler) {
    radialMenu.setClickHandler((name) -> {

      handler.handle(CombatantAction.valueOf(name));
    });
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {

    return radialMenu.touchUp(screenX, screenY, pointer, button);
  }

  @Override
  public void dispose() {
    radialMenu.dispose();
  }
}
