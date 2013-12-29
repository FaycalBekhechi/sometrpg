package com.ziodyne.sometrpg.view.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.ziodyne.sometrpg.view.components.Position;

public class PositionComponentAccessor implements TweenAccessor<Position> {
  PositionComponentAccessor() { }

  @Override
  public int getValues(Position position, int i, float[] floats) {
    floats[0] = position.getX();
    floats[1] = position.getY();
    return 2;
  }

  @Override
  public void setValues(Position position, int i, float[] floats) {
    position.setX(floats[0]);
    position.setY(floats[1]);
  }
}
