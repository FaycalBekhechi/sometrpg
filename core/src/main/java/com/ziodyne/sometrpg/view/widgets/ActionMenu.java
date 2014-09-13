package com.ziodyne.sometrpg.view.widgets;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.util.Logged;
import com.ziodyne.sometrpg.view.entities.EntityFactory;

public class ActionMenu extends InputAdapter implements Disposable, Logged {
  private final RadialMenu radialMenu;

  public ActionMenu(Set<CombatantAction> availableActions, Vector2 position, Camera camera, Engine engine, EntityFactory entityFactory) {

    List<RadialMenu.Item> actionItems = availableActions.stream()
      .map((action) -> toMenuItem(action, 360f / availableActions.size()))
      .collect(Collectors.toList());

    radialMenu = new RadialMenu(engine, entityFactory, position, camera, actionItems, 100f);
    radialMenu.render();
  }

  private RadialMenu.Item toMenuItem(CombatantAction action, float size) {
    return new RadialMenu.Item(action.name(), action.name(), size);
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
