package com.ziodyne.sometrpg.screens.stats;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.logic.models.Stat;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.UnitGrowth;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class StatChartUtils {
    private static final int GROWTH_CHART_RADIUS = 20;

    private StatChartUtils() { }

    /**
     * Get the growth radar chart for a {@link Unit}
     *
     * @param unit  The {@link Unit} for which to generate the chart.
     * @return A {@link Polygon} representing a radar chart of the growths of a unit.
     */
    public static Polygon getGrowthRadarChart(Unit unit) {
        UnitGrowth growths = unit.getGrowths();

        // The chart shows every stat except level
        Set<Stat> relevantStats = EnumSet.complementOf(EnumSet.of(Stat.LEVEL));

        // Convert growths stored as percentages to values in the interval [0,1]
        List<Float> unitGrowthRatios = new ArrayList<Float>();
        for (Stat stat : relevantStats) {
            unitGrowthRatios.add(growths.getGrowthChance(stat)/100f);
        }

        // Generate normalized vectors representing each 'point' of the radar chart
        List<Vector2> unitCircleVectors = uniformSampleUnitCircle(relevantStats.size());
        List<Vector2> scaledVertices = new ArrayList<Vector2>();

        for (int i = 0; i < relevantStats.size(); i++) {
            Vector2 directionVector = unitCircleVectors.get(i);
            Float scaleFactor = unitGrowthRatios.get(i);

            // Scale the normalized vectors along the interval [0, GROWTH_CHART_RADIUS] using the
            // growth ratio as a scaling factor
            scaledVertices.add(directionVector.mul(GROWTH_CHART_RADIUS*scaleFactor));
        }

        // Convert to LibGDX's weird polygon format
        return new Polygon(toVertexArray(scaledVertices));
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
