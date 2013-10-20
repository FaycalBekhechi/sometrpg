package com.ziodyne.sometrpg.view.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.view.screens.debug.ModelTestUtils;
import com.ziodyne.sometrpg.view.stats.charts.RadarChart;
import com.ziodyne.sometrpg.view.stats.charts.StatChart;
import com.ziodyne.sometrpg.view.tween.RadarChartAccessor;

public class ChartTestScreen implements Screen {
  private RadarChart chart;
  private OrthographicCamera camera;
  private TweenManager tweenManager;

  public ChartTestScreen() {
    camera = new OrthographicCamera(800, 400);
    camera.translate(0, 0);

    Unit testUnit = ModelTestUtils.createRandomUnit();
    chart = new StatChart(testUnit, 1);

    tweenManager = new TweenManager();

    Tween.registerAccessor(RadarChart.class, new RadarChartAccessor());
  }
  
  @Override
  public void render(float delta) {
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

    tweenManager.update(delta);

    camera.update();
    camera.zoom = 0.02f;
    camera.apply(Gdx.gl10);
    chart.render(camera);
  }

  @Override
  public void resize(int width, int height) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void show() {
    Tween.to(chart, RadarChartAccessor.RADIUS, 0.5f)
         .ease(TweenEquations.easeOutQuint)
         .target(5)
         .start(tweenManager);
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