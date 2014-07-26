package com.ziodyne.sometrpg.logic.util;

import com.badlogic.gdx.math.Vector2;

public interface BezierSpline {
  public Vector2 start();

  public Vector2 firstControlPoint();
  public Vector2 secondControlPoint();

  public Vector2 end();
}
