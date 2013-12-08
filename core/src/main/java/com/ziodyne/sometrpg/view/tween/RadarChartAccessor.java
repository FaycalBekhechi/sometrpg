package com.ziodyne.sometrpg.view.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.ziodyne.sometrpg.view.stats.charts.RadarChart;

public class RadarChartAccessor implements TweenAccessor<RadarChart> {
  public static final int RADIUS = 0;
  RadarChartAccessor() { }

  @Override
  public int getValues(RadarChart radarChart, int type, float[] values) {
    if (type == RADIUS) {
      values[0] = radarChart.getRadius();
      return 1;
    } else {
      throw new IllegalArgumentException("Invalid tween type: " + type);
    }
  }

  @Override
  public void setValues(RadarChart radarChart, int type, float[] newValues) {
    if (type == RADIUS) {
      radarChart.setRadius(newValues[0]);
    } else {
      throw new IllegalArgumentException("Invalid tween type: " + type);
    }
  }
}
