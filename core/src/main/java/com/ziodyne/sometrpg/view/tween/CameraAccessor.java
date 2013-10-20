package com.ziodyne.sometrpg.view.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.Camera;

public class CameraAccessor implements TweenAccessor<Camera> {
  public static final int POSITION = 0;

  @Override
  public int getValues(Camera target, int tweenType, float[] returnValues) {
    if (tweenType == POSITION) {
      returnValues[0] = target.position.x;
      returnValues[1] = target.position.y;
      returnValues[2] = target.position.z;
      return 3;
    } else {
      throw new IllegalArgumentException("Invalid tween type: " + tweenType);
    }

  }

  @Override
  public void setValues(Camera target, int tweenType, float[] newValues) {
    if (tweenType == POSITION) {
      target.position.set(newValues);
    } else {
      throw new IllegalArgumentException("Invalid tween type: " + tweenType);
    }
  }
}
