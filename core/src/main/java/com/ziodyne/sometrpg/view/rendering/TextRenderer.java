package com.ziodyne.sometrpg.view.rendering;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.view.entities.EntityFactory;

public class TextRenderer {

  public static enum Size {
    SIXTEEN(16),
    SIXTYFOUR(64);

    private final int size;

    Size(int size) {
      this.size = size;
    }

    public int getValue() {
      return size;
    }
  }

  private final Engine engine;
  private final EntityFactory entityFactory;

  public TextRenderer(EntityFactory entityFactory, Engine engine) {
    this.entityFactory = entityFactory;
    this.engine = engine;
  }

  public LiveEntity renderViewportText(String text, Vector2 position, Size size) {
    Entity entity = entityFactory.createViewportText(text, position, size.getValue());
    engine.addEntity(entity);

    return new LiveEntity(engine, entity);

  }

  public LiveEntity renderText(String text, Vector2 position, Size size) {
    Entity entity = entityFactory.createText(text, position, size.getValue());
    engine.addEntity(entity);

    return new LiveEntity(engine, entity);
  }
}
