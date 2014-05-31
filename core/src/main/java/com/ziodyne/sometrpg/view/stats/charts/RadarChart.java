package com.ziodyne.sometrpg.view.stats.charts;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Polygon;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.view.stats.StatChartUtils;

public abstract class RadarChart {
  private final Polygon polygon;
  private final Character character;
  protected float radius;

  public RadarChart(Character character) {
    this(character, 5f);
  }

  public RadarChart(Character character, float radius) {
    this.character = character;
    this.radius = radius;
    this.polygon = generateChart(character, radius);
  }

  protected abstract Polygon generateChart(Character character, float radius);

  public void render(Camera camera) {
    Mesh mesh = StatChartUtils.generateFanMesh(polygon);

    mesh.transform(camera.view);

    // TODO: Figure out how to render this with a basic shader.
    //mesh.render(GL20.GL_TRIANGLE_FAN);
  }

  public float getRadius() {
    return radius;
  }

  public void setRadius(float radius) {
    polygon.setScale(radius, radius);
    this.radius = radius;
  }
}
