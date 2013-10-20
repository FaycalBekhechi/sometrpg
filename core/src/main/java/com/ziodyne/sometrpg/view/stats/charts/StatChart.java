package com.ziodyne.sometrpg.view.stats.charts;

import com.badlogic.gdx.math.Polygon;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.view.stats.StatChartUtils;

public class StatChart extends RadarChart {
  public StatChart(Unit unit) {
    super(unit);
  }

  public StatChart(Unit unit, float radius) {
    super(unit, radius);
  }

  @Override
  protected Polygon generateChart(Unit unit, float radius) {
    return StatChartUtils.getStatRadarChart(unit, StatChartUtils.DEFAULT_CHARTED_STATS, radius);
  }
}
