package com.ziodyne.sometrpg.logic.util;

import com.badlogic.gdx.math.Vector2;

/**
 * A spline that approximates an arc of a circle of a given radius from [0, 90] degrees.
 */
public class CircularSplineSegmentApproximation implements BezierSpline {
  private static final float MAGIC_NUMBER = 0.5522847498f;

  private final Vector2 start;
  private final Vector2 end;

  private final Vector2 firstControlPoint;
  private final Vector2 secondControlPoint;

  public CircularSplineSegmentApproximation(float radius, float startAngle, float endAngle) {
    // Technique from "Approximation of a Cubic Bezier Curve by Circular Arcs and Vice Versa"
    // by Aleksas Riskus
    float x1, y1, x2, y2, cx1, cy1, cx2, cy2;


    float halfAngle = (degToRad(endAngle) - degToRad(startAngle)) / 2.0f;

    x2 = (float)(radius * Math.cos(halfAngle));
    y2 = (float)(radius * Math.sin(halfAngle));

    x1 = x2;
    y1 = -y2;

    float halfTan = MAGIC_NUMBER *  (float)Math.tan(halfAngle);
    cx1 = x1 + halfTan * y2;
    cy1 = y1 + halfTan * x2;

    cx2 = cx1;
    cy2 = -cy1;

    start = new Vector2(x1, y1);
    end = new Vector2(x2, y2);
    firstControlPoint = new Vector2(cx1, cy1);
    secondControlPoint = new Vector2(cx2, cy2);
  }

  private static float degToRad(float deg) {
    return (float)(deg * (Math.PI / 180f));
  }

  @Override
  public Vector2 start() {

    return start;
  }

  @Override
  public Vector2 firstControlPoint() {

    return firstControlPoint;
  }

  @Override
  public Vector2 secondControlPoint() {

    return secondControlPoint;
  }

  @Override
  public Vector2 end() {

    return end;
  }
}
