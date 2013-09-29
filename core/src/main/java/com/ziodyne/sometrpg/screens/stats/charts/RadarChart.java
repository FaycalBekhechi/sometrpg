package com.ziodyne.sometrpg.screens.stats.charts;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Polygon;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.screens.stats.StatChartUtils;

public abstract class RadarChart {
  private final Polygon polygon;
  private final Unit unit;
  protected float radius;

  public RadarChart(Unit unit) {
    this(unit, 5f);
  }

  public RadarChart(Unit unit, float radius) {
    this.unit = unit;
    this.radius = radius;
    this.polygon = generateChart(unit, radius);
  }

  protected abstract Polygon generateChart(Unit unit, float radius);

  public void render(Camera camera) {
    Mesh mesh = StatChartUtils.generateFanMesh(polygon);

    mesh.transform(camera.combined);

    mesh.render(GL10.GL_TRIANGLE_FAN);
  }
}