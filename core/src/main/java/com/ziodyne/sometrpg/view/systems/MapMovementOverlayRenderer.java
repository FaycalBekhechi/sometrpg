package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
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
public class MapMovementOverlayRenderer extends IteratingSystem {
  private static final Logger logger = new GdxLogger(MapMovementOverlayRenderer.class);
  private final ShapeRenderer shapeRenderer = new ShapeRenderer();
  private final OrthographicCamera camera;
  private final float gridSize;

  public MapMovementOverlayRenderer(OrthographicCamera camera, float gridSize) {
    super(Family.getFamilyFor(MapSquareOverlay.class));
    this.camera = camera;
    this.gridSize = gridSize;
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {

    MapSquareOverlay movementOverlay = entity.getComponent(MapSquareOverlay.class);

    Gdx.gl.glEnable(GL20.GL_BLEND);
    shapeRenderer.setProjectionMatrix(camera.projection);
    shapeRenderer.setTransformMatrix(camera.view);

    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    shapeRenderer.setColor(movementOverlay.color);

    for (GridPoint2 point : movementOverlay.points) {
      renderMovementTile(point);
    }

    shapeRenderer.end();

    Gdx.gl.glDisable(GL20.GL_BLEND);
  }

  private void renderMovementTile(GridPoint2 point) {
    float leftEdge = point.x * gridSize;
    float bottomEdge = point.y * gridSize;

    shapeRenderer.rect(leftEdge, bottomEdge, gridSize, gridSize);
  }
}
