package com.ziodyne.sometrpg.view.systems;

import java.util.Vector;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.logging.GdxLogger;
import com.ziodyne.sometrpg.logging.Logger;
import com.ziodyne.sometrpg.logic.util.BezierSpline;
import com.ziodyne.sometrpg.logic.util.CircularSplineSegmentApproximation;
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

  private final float gridSize;

  public MapMovementOverlayRenderer(OrthographicCamera camera, float gridSize) {
    super(Aspect.getAspectForAll(MapSquareOverlay.class));
    this.camera = camera;
    this.gridSize = gridSize;
  }

  @Override
  protected void process(Entity e) {
    MapSquareOverlay movementOverlay = mapOverlayMapper.get(e);

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

    drawCircle(150);
    drawCircle(100);

  }

  private void drawCircle(float radius) {

    drawArc(radius, 0, 90);
    drawArc(radius, 90, 180);
    drawArc(radius, 180, 270);
    drawArc(radius, 270, 360);

  }

  private void drawArc(float radius, float startAngle, float endAngle) {

    BezierSpline spline = new CircularSplineSegmentApproximation(radius, startAngle, endAngle);
    Bezier<Vector2> bezier = new Bezier<Vector2>(spline.start(), spline.firstControlPoint(), spline.secondControlPoint(), spline.end());
    drawBezier(bezier, startAngle);
  }

  private void drawBezier(Bezier<Vector2> bezier, float rotDegrees) {

    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.setColor(Color.WHITE);
    int segsize = 100;
    for (int i = 0; i < segsize; i++) {
      Vector2 segStart = new Vector2();
      Vector2 segEnd = new Vector2();

      bezier.valueAt(segStart, i/(float)segsize);
      bezier.valueAt(segEnd, (i+1)/(float)segsize);

      segStart.rotate(rotDegrees).add(500, 400);
      segEnd.rotate(rotDegrees).add(500, 400);

      logger.debug("Drawing from: " + segStart + " to " + segEnd);
      shapeRenderer.line(segStart, segEnd);
    }
    shapeRenderer.end();
  }

  private void renderMovementTile(GridPoint2 point) {
    float leftEdge = point.x * gridSize;
    float bottomEdge = point.y * gridSize;

    shapeRenderer.rect(leftEdge, bottomEdge, gridSize, gridSize);
  }
}
