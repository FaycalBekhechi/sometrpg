package com.ziodyne.sometrpg.math;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

public class MathUtils {
  private MathUtils() { }

  public static Vector2 interpolate(Vector2 start, Vector2 end, float a, Interpolation interpolation) {
    return interpolate(start, end, a, interpolation, interpolation);
  }

  public static Vector2 interpolate(Vector2 start, Vector2 end, float a, Interpolation xInterpolation, Interpolation yInterpolation) {
    float x = xInterpolation.apply(start.x, end.x, a);
    float y = yInterpolation.apply(start.y, start.y, a);

    return new Vector2(x, y);
  }
}
