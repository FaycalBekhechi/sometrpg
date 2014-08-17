package com.ziodyne.sometrpg.view.widgets;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Disposable;

/**
 * A disposable container designed to manage short-lived entities.
 */
class EntityRegistrar implements Disposable {
  private final Engine engine;
  private final Set<Entity> entities = new HashSet<>();

  EntityRegistrar(Engine engine) {

    this.engine = engine;
  }

  public void addEntity(Entity ent) {

    engine.addEntity(ent);
    entities.add(ent);
  }

  @Override
  public void dispose() {

    entities.stream()
      .forEach(engine::removeEntity);
  }
}
