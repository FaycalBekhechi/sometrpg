package com.ziodyne.sometrpg.screens.stats.charts;

import com.badlogic.gdx.math.Polygon;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.screens.stats.StatChartUtils;

public class GrowthChart extends RadarChart {

  public GrowthChart(Unit unit) {
    super(unit);
  }

  @Override
  protected Polygon generateChart(Unit unit) {
    return StatChartUtils.getGrowthRadarChart(unit, StatChartUtils.DEFAULT_CHARTED_STATS, radius);
  }
}
