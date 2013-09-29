package com.ziodyne.sometrpg.screens.stats.charts;

import com.badlogic.gdx.math.Polygon;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.screens.stats.StatChartUtils;

public class StatChart extends RadarChart {
  public StatChart(Unit unit) {
    super(unit);
  }

  @Override
  protected Polygon generateChart(Unit unit) {
    return StatChartUtils.getStatRadarChart(unit, StatChartUtils.DEFAULT_CHARTED_STATS, radius);
  }
}
