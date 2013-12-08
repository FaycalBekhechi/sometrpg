package com.ziodyne.sometrpg;

import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.ziodyne.sometrpg.view.tween.CameraAccessor;

public class RootModule extends AbstractModule {
  private final Game game;

  public RootModule(Game game) {
    this.game = game;
  }

  @Override
  protected void configure() {
    bind(Game.class).toInstance(game);
    bind(SpriteBatch.class).toInstance(new SpriteBatch());
    bind(TweenManager.class).toInstance(new TweenManager());
    bind(new TypeLiteral<TweenAccessor<Camera>>() {}).to(CameraAccessor.class);
  }
}
