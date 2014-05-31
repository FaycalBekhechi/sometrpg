package com.ziodyne.sometrpg.view.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ziodyne.sometrpg.logging.GdxLogger;
import com.ziodyne.sometrpg.logging.Logger;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.components.MapSquareOverlay;

/**
 * Renders {@link com.ziodyne.sometrpg.view.components.MapSquareOverlay} entities as highlighted tiles.
 */
public class MapMovementOverlayRenderer extends EntityProcessingSystem {
  private static final Logger logger = new GdxLogger(MapMovementOverlayRenderer.class);

  @Mapper
  private ComponentMapper<MapSquareOverlay> mapOverlayMapper;

  private final ShapeRenderer shapeRenderer = new ShapeRenderer();

  private final OrthographicCamera camera;

  public MapMovementOverlayRenderer(OrthographicCamera camera) {
    super(Aspect.getAspectForAll(MapSquareOverlay.class));
    this.camera = camera;
  }

  @Override
  protected void process(Entity e) {
    MapSquareOverlay movementOverlay = mapOverlayMapper.get(e);

    Gdx.gl.glEnable(GL20.GL_BLEND);
    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    shapeRenderer.setColor(movementOverlay.color);

    for (GridPoint2 point : movementOverlay.points) {
      renderMovementTile(point);
    }

    shapeRenderer.end();
    Gdx.gl.glDisable(GL20.GL_BLEND);
  }

  private void renderMovementTile(GridPoint2 point) {
    float leftEdge = point.x;
    float bottomEdge = point.y;

    shapeRenderer.rect(leftEdge, bottomEdge, 1, 1);
  }
}
