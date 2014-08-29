package com.ziodyne.sometrpg.view.rendering;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Disposable;

/**
 * Represents an entity that has been added to the system. Disposing it removes it from the system.
 */
public class LiveEntity implements Disposable {
  private final Engine engine;
  private final Entity entity;

  public LiveEntity(Engine engine, Entity entity) {
    this.engine = engine;
    this.entity = entity;
  }

  @Override
  public void dispose() {
    engine.removeEntity(entity);
  }
}
