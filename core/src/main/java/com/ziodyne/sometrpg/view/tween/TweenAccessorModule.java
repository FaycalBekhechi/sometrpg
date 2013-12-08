package com.ziodyne.sometrpg.view.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.ziodyne.sometrpg.view.stats.charts.RadarChart;

public class TweenAccessorModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(new TypeLiteral<TweenAccessor<Camera>>() {}).to(CameraAccessor.class);
    bind(new TypeLiteral<TweenAccessor<Actor>>() {}).to(ActorAccessor.class);
    bind(new TypeLiteral<TweenAccessor<RadarChart>>() {}).to(RadarChartAccessor.class);
  }
}
