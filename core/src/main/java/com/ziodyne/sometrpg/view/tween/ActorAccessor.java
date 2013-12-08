package com.ziodyne.sometrpg.view.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorAccessor implements TweenAccessor<Actor> {
  public static final int POSITION = 1;
  public static final int POSITION_Y = 2;
  public static final int OPACITY = 3;

  ActorAccessor() { }

  @Override
  public int getValues(Actor target, int tweenType, float[] returnValues) {
    switch (tweenType) {
      case OPACITY:
        Color color = target.getColor();
        returnValues[0] = color.a;
        return 1;
      case POSITION_Y:
        returnValues[0] = target.getY();
        return 1;
      case POSITION:
        returnValues[0] = target.getX();
        returnValues[1] = target.getY();
        return 2;
      default:
        throw new IllegalArgumentException("Invalid tween type: " + tweenType);
    }
  }

  @Override
  public void setValues(Actor target, int tweenType, float[] newValues) {
    switch (tweenType) {
      case OPACITY:
        target.getColor().a = newValues[0];
        break;
      case POSITION_Y:
        target.setY(newValues[0]);
        break;
      case POSITION:
        target.setPosition(newValues[0], newValues[1]);
        break;
      default:
        throw new IllegalArgumentException("Invalid tween type: " + tweenType);
    }
  }
}
