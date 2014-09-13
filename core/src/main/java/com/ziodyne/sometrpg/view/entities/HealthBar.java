package com.ziodyne.sometrpg.view.entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Disposable;

public class HealthBar implements Disposable {
  private final Engine engine;
  private final Entity entity;

  public HealthBar(Engine engine, Entity entity) {
    this.engine = engine;
    this.entity = entity;
  }

  @Override
  public void dispose() {
    engine.removeEntity(entity);
  }
}
