package com.ziodyne.sometrpg.view.stats.charts;

import com.badlogic.gdx.math.Polygon;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.view.stats.StatChartUtils;

public class GrowthChart extends RadarChart {

  public GrowthChart(Character character) {
    super(character);
  }

  public GrowthChart(Character character, float radius) {
    super(character, radius);
  }

  @Override
  protected Polygon generateChart(Character character, float radius) {
    return StatChartUtils.getGrowthRadarChart(character, StatChartUtils.DEFAULT_CHARTED_STATS, radius);
  }
}
