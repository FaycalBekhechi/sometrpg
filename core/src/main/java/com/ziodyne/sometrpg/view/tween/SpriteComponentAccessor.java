package com.ziodyne.sometrpg.view.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.ziodyne.sometrpg.view.components.SpriteComponent;

public class SpriteComponentAccessor implements TweenAccessor<SpriteComponent> {
  public static final int ALPHA = 0;

  @Override
  public int getValues(SpriteComponent target, int tweenType, float[] returnValues) {
    if (tweenType == ALPHA) {
      returnValues[0] = target.getSprite().getAlpha();
      return 1;
    }
    return 0;
  }

  @Override
  public void setValues(SpriteComponent target, int tweenType, float[] newValues) {
    if (tweenType == ALPHA) {
      target.getSprite().setAlpha(newValues[0]);
    }
  }
}
