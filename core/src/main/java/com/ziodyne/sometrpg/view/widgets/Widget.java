package com.ziodyne.sometrpg.view.widgets;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Disposable;
import com.ziodyne.sometrpg.util.Logged;

public abstract class Widget extends InputAdapter implements Disposable, Logged, Renderable {

  private final EntityRegistrar registrar;

  Widget(Engine engine) {
    registrar = new EntityRegistrar(engine);
  }

  protected void newEntity(Entity entity) {
    registrar.addEntity(entity);
  }

  @Override
  public void dispose() {
    registrar.dispose();
  }
}
