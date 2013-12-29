package com.ziodyne.sometrpg.view.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.ziodyne.sometrpg.view.components.Sprite;

public class SpriteComponentAccessor implements TweenAccessor<Sprite> {
  public static final int ALPHA = 0;

  @Override
  public int getValues(Sprite target, int tweenType, float[] returnValues) {
    if (tweenType == ALPHA) {
      returnValues[0] = target.getAlpha();
      return 1;
    }
    return 0;
  }

  @Override
  public void setValues(Sprite target, int tweenType, float[] newValues) {
    if (tweenType == ALPHA) {
      target.setAlpha(newValues[0]);
    }
  }
}
