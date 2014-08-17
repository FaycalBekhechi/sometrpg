package com.ziodyne.sometrpg.view.widgets;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.view.entities.EntityFactory;

public class UnitInfoMenu extends Widget {
  private EntityFactory entityFactory;

  public UnitInfoMenu(Engine engine, EntityFactory entityFactory) {

    super(engine);
    this.entityFactory = entityFactory;
  }

  @Override
  public void render() {

    float outerGutter = 40;
    float innerGutter = 25;

    float leftWidth = 446.5f;
    Entity smallLeft = entityFactory.createMenuBg(new Vector2(outerGutter, outerGutter), leftWidth, 630);
    Entity largeRight = entityFactory.createMenuBg(new Vector2(outerGutter + leftWidth + innerGutter, outerGutter), 1046.5f, 630);

    newEntity(smallLeft);
    newEntity(largeRight);
  }
}
