package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ziodyne.sometrpg.view.components.ShapeComponent;

public class ShapeRenderSystem extends IteratingSystem {

  private final OrthographicCamera camera;
  private final ShapeRenderer renderer = new ShapeRenderer();

  public ShapeRenderSystem(OrthographicCamera camera) {
    super(Family.getFamilyFor(ShapeComponent.class));

    this.camera = camera;
  }


  @Override
  public void processEntity(Entity entity, float deltaTime) {
    ShapeComponent shape = entity.getComponent(ShapeComponent.class);

    renderer.setProjectionMatrix(camera.combined);
    shape.execute(renderer);
  }
}
