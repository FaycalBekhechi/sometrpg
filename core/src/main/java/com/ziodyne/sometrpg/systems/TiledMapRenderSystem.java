package com.ziodyne.sometrpg.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ziodyne.sometrpg.components.TiledMapComponent;

/**
 * An Artemis system that renders TiledMapComponent components.
 */
public class TiledMapRenderSystem extends EntitySystem {
  private OrthographicCamera camera;

  @Mapper
  private ComponentMapper<TiledMapComponent> mapMapper;

  public TiledMapRenderSystem(OrthographicCamera camera) {
    super(Aspect.getAspectForAll(TiledMapComponent.class));
    this.camera = camera;
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entities) {
    for (int i = 0; i < entities.size(); i++) {
      TiledMapComponent map = mapMapper.get(entities.get(i));
      OrthogonalTiledMapRenderer renderer = map.getRenderer();

      renderer.setView(camera);
      renderer.render();
    }
  }

  @Override
  protected boolean checkProcessing() {
    return true;
  }
}
