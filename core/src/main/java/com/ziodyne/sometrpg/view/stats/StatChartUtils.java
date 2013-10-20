package com.ziodyne.sometrpg.view.stats;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ziodyne.sometrpg.logic.models.Constants;
import com.ziodyne.sometrpg.logic.models.Stat;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.UnitGrowth;
import com.ziodyne.sometrpg.logic.util.UnitUtils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class StatChartUtils {

  // The charts show every stat except level and HP
  public static final EnumSet<Stat> DEFAULT_CHARTED_STATS = EnumSet.complementOf(EnumSet.of(Stat.LEVEL, Stat.HP));

  private StatChartUtils() { }

  public static Mesh generateFanMesh(Polygon polygon) {
    MeshBuilder builder = new MeshBuilder();
    builder.begin(new VertexAttributes(new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
                                       new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, "a_color")));

    // Start the first vertex as the origin. This is intended to be rendered as a triangle fan.
    builder.vertex(new float[]{ 0, 0, 0, Color.toFloatBits(0, 0, 255, 255) });

    // Generate vertices for the remaining points.
    float[] transformedVertices = polygon.getTransformedVertices();
    for (int first = 0, second = 1; first < transformedVertices.length; first+=2, second+=2) {
      float x = transformedVertices[first];
      float y = transformedVertices[second];
      float z = 0;

      builder.vertex(new float[]{ x, y, z, Color.toFloatBits(255, 0, 0, 255) });
    }

    // Use indices to organize the vertices properly in a fan.
    int numVertices = transformedVertices.length/2;
    for (int i = 1; i <= numVertices; i++) {
      if (i == numVertices) {
        builder.index((short)0, (short)i, (short)1);
      } else {
        builder.index((short)0, (short)i, (short)(i+1));
      }
    }

    return builder.end();
  }

  /**
   * Get the growth radar chart for a {@link Unit}
   *
   * @param unit  The {@link Unit} for which to generate the chart.
   * @return A {@link Polygon} representing a radar chart of the growths of a unit.
   */
  public static Polygon getGrowthRadarChart(Unit unit, EnumSet<Stat> chartedStats, float radius) {
    UnitGrowth growths = unit.getGrowths();

    // Convert growths stored as percentages to values in the interval [0,1]
    List<Float> unitGrowthRatios = new ArrayList<Float>(chartedStats.size());
    for (Stat stat : chartedStats) {
      unitGrowthRatios.add(growths.getGrowthChance(stat)/100f);
    }

    List<Vector2> scaledVertices = getScaledChartVertices(unitGrowthRatios, 1);

    Polygon result = new Polygon(toVertexArray(scaledVertices));
    
    result.setScale(radius, radius);
    
    return result;
  }

  /**
   * Get the stat value radar chart for a {@link Unit}
   *
   * @param unit The {@link Unit} for which to generate the chart.
   * @return A {@link Polygon} representing a radar chart of the stats of a unit.
   */
  public static Polygon getStatRadarChart(Unit unit, EnumSet<Stat> chartedStats, float radius) {
    Map<Stat, Integer> statSheet = UnitUtils.indexStatSheetByValue(unit.getStatSheet());

    List<Float> unitStatRatios = new ArrayList<Float>(chartedStats.size());
    for (Stat stat : chartedStats) {
      unitStatRatios.add((float)statSheet.get(stat)/ Constants.STAT_MAX);
    }

    List<Vector2> scaledVertices = getScaledChartVertices(unitStatRatios, radius);

    return new Polygon(toVertexArray(scaledVertices));
  }

  /**
   * Get the stat cap radar chart for a {@link Unit}
   *
   * @param unit The {@link Unit} for which to generate the chart.
   * @return A {@link Polygon} representing a radar chart of the stats of a unit.
   */
  public static Polygon getMaxStatRadarChart(Unit unit, EnumSet<Stat> chartedStats, float radius) {
    Map<Stat, Integer> maxStatSheet = UnitUtils.indexStatSheetByValue(unit.getMaxStatSheet());

    List<Float> unitMaxStatRatios = new ArrayList<Float>(chartedStats.size());
    for (Stat stat : chartedStats) {
      unitMaxStatRatios.add((float)maxStatSheet.get(stat)/Constants.STAT_MAX);
    }

    List<Vector2> scaledVertices = getScaledChartVertices(unitMaxStatRatios, radius);

    return new Polygon(toVertexArray(scaledVertices));
  }

  /**
   * Generate vectors representing the points of the radar chart.
   *
   * @param scalingFactors A {@link List} of floating point scaling factors to use against the radar chart's radius for
   *                       vector length.
   * @param radius The radius of the chart.
   *
   * @return A {@link List} of properly scaled {@link Vector2}s.
   */
   static List<Vector2> getScaledChartVertices(List<Float> scalingFactors, float radius) {

    // Generate normalized vectors representing each 'point' of the radar chart
    List<Vector2> unitCircleVectors = uniformSampleUnitCircle(scalingFactors.size());
    List<Vector2> scaledVertices = new ArrayList<Vector2>();

    for (int i = 0; i < scalingFactors.size(); i++) {
      Vector2 directionVector = unitCircleVectors.get(i);
      Float scaleFactor = scalingFactors.get(i);

      // Generate a properly-scaled vector to the real point in the radar chart
      scaledVertices.add(directionVector.scl(radius*scaleFactor));
    }

    return scaledVertices;
  }

  /**
   * Convert a {@link List} of {@link Vector2} to a vertex array.
   *
   * @param vectors Vectors to convert
   * @return An array of floats where every pair of entries corresponds to the x and y coordinates of a point.
   */
  static float[] toVertexArray(List<Vector2> vectors) {
    float[] vertices = new float[vectors.size()*2];

    for (int i = 0, j = 0; i < vectors.size(); i+=1, j+=2) {
      Vector2 vector = vectors.get(i);

      vertices[j] = vector.x;
      vertices[j+1] = vector.y;
    }

    return vertices;
  }

  /**
   * Generate a uniform sampling of vectors corresponding to points on the unit circle.
   *
   * @param numSamples The number of samples to generate
   * @return A list of two-dimensional vectors
   */
  static List<Vector2> uniformSampleUnitCircle(int numSamples) {
    List<Vector2> samples = new ArrayList<Vector2>();

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
}
