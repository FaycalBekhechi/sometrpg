package com.ziodyne.sometrpg.view.input;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.view.tween.CameraAccessor;

public class CameraMoveController extends InputAdapter {
  private static final int DRAG_TOLERANCE = 4;
  private final OrthographicCamera camera;
  private final TweenManager tweenManager;
  private boolean ignoreNextTouchUp = false;

  public interface Factory {
    public CameraMoveController create(OrthographicCamera camera);
  }

  @AssistedInject
  CameraMoveController(@Assisted OrthographicCamera camera, TweenManager tweenManager) {
    this.camera = camera;
    this.tweenManager = tweenManager;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (button == Input.Buttons.LEFT && !ignoreNextTouchUp) {

      // Get the touch position in world space.
      Vector3 touchCoords3d = new Vector3(screenX, screenY, 0);
      camera.unproject(touchCoords3d);

      // Slide the camera to the new position.
      Tween.to(camera, CameraAccessor.POSITION, 0.5f)
              .target(touchCoords3d.x, touchCoords3d.y, 0)
              .start(tweenManager);
    }
    ignoreNextTouchUp = false;
    return false;
  }

  @Override
  public boolean touchDragged(int i, int i2, int i3) {
    int dx = Gdx.input.getDeltaX();
    int dy = Gdx.input.getDeltaY();

    if (Math.abs(dx) < DRAG_TOLERANCE && Math.abs(dy) < DRAG_TOLERANCE) {
      return false;
    }

    camera.translate(new Vector3(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY(), 0));
    ignoreNextTouchUp = true;

    return true;
  }

  @Override
  public boolean scrolled(int amount) {
    camera.zoom += amount * 0.1;

    return false;
  }
}
