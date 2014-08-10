package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ziodyne.sometrpg.view.components.Shader;
import com.ziodyne.sometrpg.view.components.TiledMapComponent;

/**
 * An Artemis system that renders TiledMapComponent components.
 */
public class TiledMapRenderSystem extends IteratingSystem {
  private OrthographicCamera camera;

  private ShaderProgram defaultShader;

  public TiledMapRenderSystem(OrthographicCamera camera) {
    super(Family.getFamilyFor(TiledMapComponent.class));
    this.camera = camera;
    this.defaultShader = SpriteBatch.createDefaultShader();
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {

    TiledMapComponent map = entity.getComponent(TiledMapComponent.class);
    OrthogonalTiledMapRenderer renderer = map.getRenderer();

    renderer.setView(camera);

    ShaderProgram program = defaultShader;
    if (entity.hasComponent(Shader.class)) {
      Shader shaderComponent = entity.getComponent(Shader.class);
      // Allow the shader parameters to be updated with frame-dependant (and time-dependant data)
      shaderComponent.update(deltaTime);
      program = shaderComponent.getShader();
    }

    renderer.getSpriteBatch().setShader(program);

    renderer.render();
  }
}
