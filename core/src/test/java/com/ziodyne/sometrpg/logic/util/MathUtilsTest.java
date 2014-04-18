package com.ziodyne.sometrpg.logic.util;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import org.junit.Assert;
import org.junit.Test;

public class MathUtilsTest {


  static void assertEpsilonEquals(Vector2 expected, Vector2 actual) {
    if (!expected.epsilonEquals(actual, 0.00001f)) {
      Assert.fail();
    }
  }

  @Test
  public void testUnitCircleSampling() {
    int nSamples = 2;
    List<Vector2> samples = MathUtils.uniformSampleUnitCircle(nSamples);

    Assert.assertEquals(2, samples.size());

    Vector2 first = samples.get(0);
    Vector2 second = samples.get(1);

    Vector2 correctFirst = new Vector2(0, 1);
    Vector2 correctSecond = new Vector2(0, -1);

    assertEpsilonEquals(correctFirst, first);
    assertEpsilonEquals(correctSecond, second);
  }
}
