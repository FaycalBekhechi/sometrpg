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
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Matrix4;
import com.ziodyne.sometrpg.logging.GdxLogger;
import com.ziodyne.sometrpg.logging.Logger;
import com.ziodyne.sometrpg.view.components.MapGridOverlay;
import com.ziodyne.sometrpg.view.components.Position;

public class MapOverlayRenderSystem extends EntityProcessingSystem {
  private final Logger logger = new GdxLogger(MapOverlayRenderSystem.class);

  private final ShapeRenderer shapeRenderer = new ShapeRenderer();

  @Mapper
  private ComponentMapper<MapGridOverlay> mapOverlayComponentMapper;

  @Mapper
  private ComponentMapper<Position> positionComponentMapper;

  private OrthographicCamera camera;


  public MapOverlayRenderSystem(OrthographicCamera camera) {
    super(Aspect.getAspectForAll(MapGridOverlay.class, Position.class));
    this.camera = camera;
  }

  @Override
  protected void process(Entity entity) {
    Position position = positionComponentMapper.get(entity);
    MapGridOverlay overlay = mapOverlayComponentMapper.get(entity);

    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.setColor(0, 0, 0, overlay.opacity);

    float size = overlay.gridSquareSize;
    Matrix4 transform = new Matrix4().scale(size, size, 1f);
    shapeRenderer.setTransformMatrix(transform);

    // Blending comes at a slight performance cost, so only enable it if it's necessary.
    if (overlay.opacity < 1.0f) {
      Gdx.gl.glEnable(GL20.GL_BLEND);
    }

    // Render row lines
    GridPoint2 currentStartPoint = new GridPoint2(Math.round(position.getX()), Math.round(position.getY()));
    for (int row = 0; row <= overlay.rows; row++) {

      float x1 = currentStartPoint.x;
      float y1 = currentStartPoint.y;

      float x2 = currentStartPoint.x + overlay.rows;
      float y2 = currentStartPoint.y;

      shapeRenderer.line(x1, y1, x2, y2);
      currentStartPoint.set(currentStartPoint.x, currentStartPoint.y+1);
    }

    // Render column lines
    GridPoint2 colStartPoint = new GridPoint2(Math.round(position.getX()), Math.round(position.getY()));
    for (int col = 0; col <= overlay.columns; col++) {
      float x1 = colStartPoint.x;
      float y1 = colStartPoint.y;

      float x2 = colStartPoint.x;
      float y2 = colStartPoint.y + overlay.columns;

      shapeRenderer.line(x1, y1, x2, y2);
      colStartPoint.set(colStartPoint.x+1, colStartPoint.y);
    }

    shapeRenderer.end();

    // Disable blending if we enabled it before
    if (overlay.opacity < 1.0f) {
      Gdx.gl.glDisable(GL20.GL_BLEND);
    }
  }
}
