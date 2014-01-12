package com.ziodyne.sometrpg.view.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.ziodyne.sometrpg.view.components.MapMovementOverlay;

/**
 * Renders {@link MapMovementOverlay} entities as highlighted tiles.
 */
public class MapMovementOverlayRenderer extends EntityProcessingSystem {
  @Mapper
  private ComponentMapper<MapMovementOverlay> mapOverlayMapper;

  private final ShapeRenderer shapeRenderer = new ShapeRenderer();

  private final OrthographicCamera camera;

  public MapMovementOverlayRenderer(OrthographicCamera camera) {
    super(Aspect.getAspectForAll(MapMovementOverlay.class));
    this.camera = camera;
  }

  @Override
  protected void process(Entity e) {
    MapMovementOverlay movementOverlay = mapOverlayMapper.get(e);

    Gdx.gl.glEnable(GL10.GL_BLEND);
    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    shapeRenderer.setColor(movementOverlay.color);

    for (GridPoint2 point : movementOverlay.points) {
      renderMovementTile(point);
    }

    shapeRenderer.end();
    Gdx.gl.glDisable(GL10.GL_BLEND);
  }

  private void renderMovementTile(GridPoint2 point) {
    float leftEdge = point.x;
    float bottomEdge = point.y;

    shapeRenderer.rect(leftEdge, bottomEdge, 1, 1);
  }
}
