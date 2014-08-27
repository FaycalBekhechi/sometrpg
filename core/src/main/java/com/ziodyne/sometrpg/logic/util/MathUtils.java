package com.ziodyne.sometrpg.logic.util;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MathUtils {

  private MathUtils() { }

  public static Vector2 rotateAroundPoint(Vector2 center, Vector2 point, float degrees) {

    float rotRadians = com.badlogic.gdx.math.MathUtils.degreesToRadians * degrees;
    float xOrigin = center.x;
    float yOrigin = center.y;
    float x = point.x;
    float y = point.y;

    float resultX = ((x - xOrigin) * (float)Math.cos(rotRadians)) -
            ((yOrigin - y) * (float)Math.sin(rotRadians)) + xOrigin;

    float resultY = (-(yOrigin - y) * (float)Math.cos(rotRadians)) -
            ((x - xOrigin) * (float)Math.sin(rotRadians)) + yOrigin;

    return new Vector2(resultX, resultY);
  }

  /**
   * Generate a uniform sampling of vectors corresponding to points on the unit circle.
   *
   * @param numSamples The number of samples to generate
   * @return A list of two-dimensional vectors
   */
  public static List<Vector2> uniformSampleUnitCircle(int numSamples) {
    List<Vector2> samples = new ArrayList<>();

    if (numSamples <= 0) {
      return samples;
    }

    float sampleFrequencyDegrees = 360f/numSamples;

    // Generate samples along the unit circle by rotating the normalized 'up' vector
    // around the circle at evenly-distributed intervals.
    Vector3 startPoint = new Vector3(0f, 1f, 0f);
    Matrix4 rotation = new Matrix4();

    for (int i = 0; i <= numSamples-1; i++) {
      Vector3 rotated = startPoint.cpy();

      rotation.setToRotation(0f, 0f, 1f, sampleFrequencyDegrees*i);

      rotated.rot(rotation);
      samples.add(new Vector2(rotated.x, rotated.y));
    }

    return samples;
  }

  public static float nearestMultipleOf(float multiple, float number) {
    return multiple*((float)Math.floor(number/multiple));
  }

  public static Set<GridPoint2> getNeighbors(GridPoint2 start) {
    return Sets.newHashSet(
            MathUtils.getEastNeighbor(start),
            MathUtils.getWestNeighbor(start),
            MathUtils.getNorthNeighbor(start),
            MathUtils.getSouthNeighbor(start)
    );
  }

  public static int manhattanDistance(GridPoint2 start, GridPoint2 end) {
    return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
  }

  public static GridPoint2 getNorthNeighbor(GridPoint2 point) {
    return new GridPoint2(point.x, point.y + 1);
  }

  public static GridPoint2 getSouthNeighbor(GridPoint2 point) {
    return new GridPoint2(point.x, point.y - 1);
  }

  public static GridPoint2 getWestNeighbor(GridPoint2 point) {
    return new GridPoint2(point.x - 1, point.y);
  }

  public static GridPoint2 getEastNeighbor(GridPoint2 point) {
    return new GridPoint2(point.x + 1, point.y);
  }
}
