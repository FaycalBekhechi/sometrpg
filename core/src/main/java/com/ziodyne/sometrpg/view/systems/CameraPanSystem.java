package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.util.Logged;
import com.ziodyne.sometrpg.view.input.Toggleable;

/**
 * This class handles moving the camera based on user input.
 */
public class CameraPanSystem extends EntitySystem implements Logged, Toggleable {
  private static final float CAMERA_PAN_SPEED = 2f;
  private static final float VIEW_POS_NUDGE = 0.001f;

  private final OrthographicCamera camera;
  private final Rectangle mapBoundingRect;

  private boolean enabled = true;

  public CameraPanSystem(OrthographicCamera camera, Rectangle mapBoundingRect) {
    this.camera = camera;
    this.mapBoundingRect = mapBoundingRect;
  }

  @Override
  public void enable() {
    enabled = true;
  }

  @Override
  public void disable() {
    enabled = false;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public boolean checkProcessing() {
    return enabled;
  }

  @Override
  public void update(float deltaTime) {
    super.update(deltaTime);

    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      panIfPossible(0, CAMERA_PAN_SPEED);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      panIfPossible(0, -CAMERA_PAN_SPEED);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      panIfPossible(-CAMERA_PAN_SPEED, 0);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      panIfPossible(CAMERA_PAN_SPEED, 0);
    }
  }

  private boolean canMoveBy(float x, float y) {
    Vector2 camPos = new Vector2(camera.position.x, camera.position.y);
    Vector2 newPos = camPos.cpy().add(x, y);
    float trueViewHeight = camera.viewportHeight * camera.zoom;
    float trueViewWidth = camera.viewportWidth * camera.zoom;

    // We do not allow the position of the rectangle to actually be (0, 0) because LibGDX considers
    // perfectly overlapping rectangles to not contain each-other, which makes sense.
    Rectangle newViewBounds = new Rectangle(
      (newPos.x - (trueViewWidth)/2) + VIEW_POS_NUDGE,
      (newPos.y - (trueViewHeight)/2) + VIEW_POS_NUDGE,
      trueViewWidth,
      trueViewHeight
    );

    return mapBoundingRect.contains(newViewBounds);
  }

  private void panIfPossible(float x, float y) {
    if (canMoveBy(x, y)) {
      camera.translate(x, y);
    }
  }
}
