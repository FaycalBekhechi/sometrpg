package com.ziodyne.sometrpg.view.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ziodyne.sometrpg.view.components.Shader;
import com.ziodyne.sometrpg.view.components.TiledMapComponent;

/**
 * An Artemis system that renders TiledMapComponent components.
 */
public class TiledMapRenderSystem extends EntitySystem {
  private OrthographicCamera camera;

  @Mapper
  private ComponentMapper<TiledMapComponent> mapMapper;

  @Mapper
  private ComponentMapper<Shader> shaderMapper;

  private ShaderProgram defaultShader;

  public TiledMapRenderSystem(OrthographicCamera camera) {
    super(Aspect.getAspectForAll(TiledMapComponent.class));
    this.camera = camera;
    this.defaultShader = SpriteBatch.createDefaultShader();
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entities) {
    for (int i = 0; i < entities.size(); i++) {
      Entity entity = entities.get(i);
      TiledMapComponent map = mapMapper.get(entity);
      OrthogonalTiledMapRenderer renderer = map.getRenderer();

      renderer.setView(camera);

      ShaderProgram program = defaultShader;
      Shader shaderComponent = shaderMapper.getSafe(entity);
      if (shaderComponent != null) {

        // Allow the shader parameters to be updated with frame-dependant (and time-dependant data)
        shaderComponent.update(world.getDelta());
        program = shaderComponent.getShader();
      }

      renderer.getSpriteBatch().setShader(program);

      renderer.render();
    }
  }

  @Override
  protected boolean checkProcessing() {
    return true;
  }
}
