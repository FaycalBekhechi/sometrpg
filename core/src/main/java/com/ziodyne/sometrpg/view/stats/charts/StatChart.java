package com.ziodyne.sometrpg.view.stats.charts;

import com.badlogic.gdx.math.Polygon;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.view.stats.StatChartUtils;

public class StatChart extends RadarChart {
  public StatChart(Character character) {
    super(character);
  }

  public StatChart(Character character, float radius) {
    super(character, radius);
  }

  @Override
  protected Polygon generateChart(Character character, float radius) {
    return StatChartUtils.getStatRadarChart(character, StatChartUtils.DEFAULT_CHARTED_STATS, radius);
  }
}
