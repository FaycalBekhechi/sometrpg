package com.ziodyne.sometrpg.view.stats.charts;

import com.badlogic.gdx.math.Polygon;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.view.stats.StatChartUtils;

public class MaxStatChart extends RadarChart {
  public MaxStatChart(Character character) {
    super(character);
  }

  public MaxStatChart(Character character, float radius) {
    super(character, radius);
  }

  @Override
  protected Polygon generateChart(Character character, float radius) {
    return StatChartUtils.getMaxStatRadarChart(character, StatChartUtils.DEFAULT_CHARTED_STATS, radius);
  }
}
