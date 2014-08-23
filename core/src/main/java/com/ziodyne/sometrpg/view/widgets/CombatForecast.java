package com.ziodyne.sometrpg.view.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatSummary;
import com.ziodyne.sometrpg.view.entities.EntityFactory;

public class CombatForecast extends Widget {
  private static final String CANCEL_MENU_ID = "cancel";
  private static final String SELECT_MENU_ID = "select";
  private final RadialMenu menu;

  public static enum Action {
    CONFIRM,
    REJECT
  }

  public CombatForecast(Engine engine, EntityFactory entityFactory, Vector2 position, OrthographicCamera camera,
                        CombatSummary summary) {
    super(engine);
    List<RadialMenu.Item> items = new ArrayList<>();
    items.add(new RadialMenu.Item("attacker", 145));
    items.add(new RadialMenu.Item(SELECT_MENU_ID, 35));
    items.add(new RadialMenu.Item(CANCEL_MENU_ID, 35));
    items.add(new RadialMenu.Item("defender", 145));

    menu = new RadialMenu(engine, entityFactory, position, camera, items);
  }

  public void addSelectedHandler(Consumer<Action> handler) {
    menu.setClickHandler((action) -> {
      switch (action) {
        case CANCEL_MENU_ID:
          handler.accept(Action.REJECT);
          break;
        case SELECT_MENU_ID:
          handler.accept(Action.CONFIRM);
          break;
      }
    });
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {

    return menu.touchUp(screenX, screenY, pointer, button);
  }

  @Override
  public void render() {
    menu.render();
  }

  @Override
  public void dispose() {
    menu.dispose();
  }
}
