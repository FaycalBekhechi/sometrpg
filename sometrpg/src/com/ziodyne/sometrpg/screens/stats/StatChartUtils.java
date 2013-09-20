package com.ziodyne.sometrpg.screens.stats;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.logic.models.*;

import java.util.*;
import java.util.Map;

public class StatChartUtils {
  private static final int GROWTH_CHART_RADIUS = 20;

  // The charts show every stat except level and HP
  private static final EnumSet<Stat> CHARTED_STATS = EnumSet.complementOf(EnumSet.of(Stat.LEVEL, Stat.HP));

  private StatChartUtils() { }

  /**
   * Get the growth radar chart for a {@link Unit}
   *
   * @param unit  The {@link Unit} for which to generate the chart.
   * @return A {@link Polygon} representing a radar chart of the growths of a unit.
   */
  public static Polygon getGrowthRadarChart(Unit unit) {
    UnitGrowth growths = unit.getGrowths();

    // Convert growths stored as percentages to values in the interval [0,1]
    List<Float> unitGrowthRatios = new ArrayList<Float>(CHARTED_STATS.size());
    for (Stat stat : CHARTED_STATS) {
      unitGrowthRatios.add(growths.getGrowthChance(stat)/100f);
    }

    List<Vector2> unitCircleVectors = uniformSampleUnitCircle(CHARTED_STATS.size());
    List<Vector2> scaledVertices = getScaledChartVertices(unitGrowthRatios);

    // Convert to LibGDX's weird polygon format
    return new Polygon(toVertexArray(scaledVertices));
  }

  /**
   * Get the stat value radar chart for a {@link Unit}
   *
   * @param unit The {@link Unit} for which to generate the chart.
   * @return A {@link Polygon} representing a radar chart of the stats of a unit.
   */
  public static Polygon getStatRadarChart(Unit unit) {
    Map<Stat, Integer> statSheet = indexStatSheetByValue(unit.getStatSheet());

    List<Float> unitStatRatios = new ArrayList<Float>(CHARTED_STATS.size());
    for (Stat stat : CHARTED_STATS) {
      unitStatRatios.add((float)statSheet.get(stat)/Constants.STAT_MAX);
    }

    List<Vector2> scaledVertices = getScaledChartVertices(unitStatRatios);

    return new Polygon(toVertexArray(scaledVertices));
  }

  /**
   * Get the stat cap radar chart for a {@link Unit}
   *
   * @param unit The {@link Unit} for which to generate the chart.
   * @return A {@link Polygon} representing a radar chart of the stats of a unit.
   */
  public static Polygon getMaxStatRadarChart(Unit unit) {
    Map<Stat, Integer> maxStatSheet = indexStatSheetByValue(unit.getMaxStatSheet());

    List<Float> unitMaxStatRatios = new ArrayList<Float>(CHARTED_STATS.size());
    for (Stat stat : CHARTED_STATS) {
      unitMaxStatRatios.add((float)maxStatSheet.get(stat)/Constants.STAT_MAX);
    }

    List<Vector2> scaledVertices = getScaledChartVertices(unitMaxStatRatios);

    return new Polygon(toVertexArray(scaledVertices));
  }

  /**
   * Generate vectors representing the points of the radar chart.
   *
   * @param scalingFactors A {@link List} of floating point scaling factors to use against the radar chart's radius for
   *                       vector length.
   *
   * @return A {@link List} of properly scaled {@link Vector2}s.
   */
  private static List<Vector2> getScaledChartVertices(List<Float> scalingFactors) {

    // Generate normalized vectors representing each 'point' of the radar chart
    List<Vector2> unitCircleVectors = uniformSampleUnitCircle(scalingFactors.size());
    List<Vector2> scaledVertices = new ArrayList<Vector2>();

    for (int i = 0; i < CHARTED_STATS.size(); i++) {
      Vector2 directionVector = unitCircleVectors.get(i);
      Float scaleFactor = scalingFactors.get(i);

      // Generate a properly-scaled vector to the real point in the radar chart
      scaledVertices.add(directionVector.mul(GROWTH_CHART_RADIUS*scaleFactor));
    }

    return scaledVertices;
  }

  /**
   * Get an index of a {@link Unit}'s stat values by stat type.
   *
   * @param stats A {@link Collection} of {@link UnitStat} to index
   * @return A {@link Map} of {@link Stat} to {@link Integer} representing the unit's value in the given stat.
   */
  private static Map<Stat, Integer> indexStatSheetByValue(Collection<UnitStat> stats) {
    Map<Stat, Integer> index = new HashMap<Stat, Integer>();

    for (UnitStat stat : stats) {
      index.put(stat.getStat(), stat.getValue());
    }

    return index;
  }

  /**
   * Convert a {@link List} of {@link Vector2} to a vertex array.
   *
   * @param vectors Vectors to convert
   * @return An array of floats where every pair of entries corresponds to the x and y coordinates of a point.
   */
  private static float[] toVertexArray(List<Vector2> vectors) {
    float[] vertices = new float[vectors.size()*2];

    for (int i = 0; i < vectors.size(); i+=2) {
      Vector2 vector = vectors.get(i);

      vertices[i] = vector.x;
      vertices[i+1] = vector.y;
    }

    return vertices;
  }

  /**
   * Generate a uniform sampling of vectors corresponding to points on the unit circle.
   *
   * @param numSamples The number of samples to generate
   * @return A list of two-dimensional vectors
   */
  private static List<Vector2> uniformSampleUnitCircle(int numSamples) {
    List<Vector2> samples = new ArrayList<Vector2>();

    if (numSamples <= 0) {
      return samples;
    }

    float sampleFrequencyDegrees = 360f/numSamples;

    // Generate samples along the unit circle by rotating the normalized 'up' vector
    // around the circle at evenly-distributed intervals.
    Vector2 startPoint = new Vector2(0, 1);
    for (int i = 0; i < numSamples-1; i++) {
      Matrix3 rotation = new Matrix3();

      rotation.rotate(sampleFrequencyDegrees*i);

      samples.add(startPoint.mul(rotation));
    }

    return samples;
  }
}
