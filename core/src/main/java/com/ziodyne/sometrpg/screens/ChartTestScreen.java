package com.ziodyne.sometrpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.screens.debug.ModelTestUtils;
import com.ziodyne.sometrpg.screens.stats.charts.RadarChart;
import com.ziodyne.sometrpg.screens.stats.charts.StatChart;

public class ChartTestScreen implements Screen {
  private RadarChart chart;
  private OrthographicCamera camera;
  
  public ChartTestScreen() {
    camera = new OrthographicCamera(800, 400);
    camera.translate(0, 0);

    Unit testUnit = ModelTestUtils.createMaxedUnit();
    chart = new StatChart(testUnit);
  }
  
  @Override
  public void render(float delta) {
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

    camera.update();
    camera.zoom = 0.01f;
    camera.apply(Gdx.gl10);
    chart.render(camera);
  }

  @Override
  public void resize(int width, int height) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void show() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void hide() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void pause() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void resume() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void dispose() {
    // TODO Auto-generated method stub
    
  }

}
