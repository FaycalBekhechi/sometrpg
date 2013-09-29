package com.ziodyne.sometrpg.screens.stats.charts;

import com.badlogic.gdx.math.Polygon;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.screens.stats.StatChartUtils;

public class GrowthChart extends RadarChart {

  public GrowthChart(Unit unit) {
    super(unit);
  }

  public GrowthChart(Unit unit, float radius) {
    super(unit, radius);
  }

  @Override
  protected Polygon generateChart(Unit unit, float radius) {
    return StatChartUtils.getGrowthRadarChart(unit, StatChartUtils.DEFAULT_CHARTED_STATS, radius);
  }
}
