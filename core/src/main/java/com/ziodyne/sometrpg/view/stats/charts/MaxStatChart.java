package com.ziodyne.sometrpg.view.stats.charts;

import com.badlogic.gdx.math.Polygon;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.view.stats.StatChartUtils;

public class MaxStatChart extends RadarChart {
  public MaxStatChart(Unit unit) {
    super(unit);
  }

  public MaxStatChart(Unit unit, float radius) {
    super(unit, radius);
  }

  @Override
  protected Polygon generateChart(Unit unit, float radius) {
    return StatChartUtils.getMaxStatRadarChart(unit, StatChartUtils.DEFAULT_CHARTED_STATS, radius);
  }
}
