package com.ziodyne.sometrpg.input;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.ziodyne.sometrpg.tween.CameraAccessor;

public class CameraMoveController extends DefaultInputProcessor {
  private final OrthographicCamera camera;
  private final World world;
  private final TweenManager tweenManager;
  private boolean ignoreNextTouchUp = false;

  public CameraMoveController(OrthographicCamera camera, World world, TweenManager tweenManager) {
    this.camera = camera;
    this.world = world;
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
