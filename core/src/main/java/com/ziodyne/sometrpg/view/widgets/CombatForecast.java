package com.ziodyne.sometrpg.view.widgets;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatSummary;
import com.ziodyne.sometrpg.view.entities.EntityFactory;

public class CombatForecast extends Widget {
  private final RadialMenu menu;

  public CombatForecast(Engine engine, EntityFactory entityFactory, Vector2 position, OrthographicCamera camera,
                        CombatSummary summary) {
    super(engine);
    List<RadialMenu.Item> items = new ArrayList<>();
    items.add(new RadialMenu.Item("attacker", 145));
    items.add(new RadialMenu.Item("select", 35));
    items.add(new RadialMenu.Item("cancel", 35));
    items.add(new RadialMenu.Item("defender", 145));

    menu = new RadialMenu(engine, entityFactory, position, camera, items);
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
