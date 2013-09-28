package com.ziodyne.sometrpg.screens.stats;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Lists;

public class StatChartUtilsTest {
  
  private static float epsilon = 0.00001f;

  @Test
  public void test() {
    int radius = 5;
    List<Float> scalingFactors = Lists.newArrayList(0.5f, 0.5f);
    List<Vector2> vectors = StatChartUtils.getScaledChartVertices(scalingFactors, radius);
    
    System.out.println(vectors);
    assertEquals(scalingFactors.size(), vectors.size());
  }
  
  @Test
  public void testUnitCircleSampling() {
    int nSamples = 2;
    List<Vector2> samples = StatChartUtils.uniformSampleUnitCircle(nSamples);
    
    assertEquals(2, samples.size());
    
    Vector2 first = samples.get(0);
    Vector2 second = samples.get(1);
    
    Vector2 correctFirst = new Vector2(0, 1);
    Vector2 correctSecond = new Vector2(0, -1);
    
    if (!first.epsilonEquals(correctFirst, epsilon)) {
      fail();
    }
    
    if (!second.epsilonEquals(correctSecond, epsilon)) {
      fail();
    }
  }

}
