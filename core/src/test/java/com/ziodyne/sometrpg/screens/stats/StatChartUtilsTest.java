package com.ziodyne.sometrpg.screens.stats;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ziodyne.sometrpg.logic.models.ModelTestUtils;
import com.ziodyne.sometrpg.logic.models.Unit;

public class StatChartUtilsTest {
  
  private static float epsilon = 0.00001f;

  static void assertEpsilonEquals(Vector2 expected, Vector2 actual, float epsilon) {
    if (!expected.epsilonEquals(actual, epsilon)) {
      Assert.fail();
    }
  }
  
  @Test
  public void testGrowthPolygonGeneration() {
    Unit testUnit = ModelTestUtils.createUnit();
    
    int radius = 5;
    Polygon radarChart = StatChartUtils.getGrowthRadarChart(testUnit, StatChartUtils.DEFAULT_CHARTED_STATS, radius);
    
    // We expect 2 vertices per stat because the 2d vertices are stored in a flattened array
    Assert.assertEquals(radarChart.getVertices().length, StatChartUtils.DEFAULT_CHARTED_STATS.size()*2);
  }
  
  @Test
  public void testVertexScaling() {
    int radius = 5;
    List<Float> scalingFactors = Lists.newArrayList(0.5f, 0.5f);
    List<Vector2> vectors = StatChartUtils.getScaledChartVertices(scalingFactors, radius);
    Assert.assertEquals(scalingFactors.size(), vectors.size());

    
    Vector2 expectedFirst = new Vector2(0, 1).scl(radius*0.5f);
    Vector2 expectedSecond = new Vector2(0, -1).scl(radius*0.5f);
    
    Vector2 actualFirst = vectors.get(0);
    Vector2 actualSecond = vectors.get(1);
    
    assertEpsilonEquals(expectedFirst, actualFirst, epsilon);
    assertEpsilonEquals(expectedSecond, actualSecond, epsilon);
  }
  
  @Test
  public void testUnitCircleSampling() {
    int nSamples = 2;
    List<Vector2> samples = StatChartUtils.uniformSampleUnitCircle(nSamples);
    
    Assert.assertEquals(2, samples.size());
    
    Vector2 first = samples.get(0);
    Vector2 second = samples.get(1);
    
    Vector2 correctFirst = new Vector2(0, 1);
    Vector2 correctSecond = new Vector2(0, -1);
    
    assertEpsilonEquals(correctFirst, first, epsilon);
    assertEpsilonEquals(correctSecond, second, epsilon);
  }

  @Test
  public void testVertexArrayConversion() {
    float[] vertices = new float[]{0f, 1f, 2f, 3f};
    List<Vector2> vectors = Lists.newArrayList(new Vector2(vertices[0], vertices[1]), new Vector2(vertices[2], vertices[3]));
    
    Assert.assertArrayEquals(vertices, StatChartUtils.toVertexArray(vectors), epsilon);
  }
}
