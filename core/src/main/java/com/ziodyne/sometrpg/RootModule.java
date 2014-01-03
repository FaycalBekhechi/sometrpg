package com.ziodyne.sometrpg;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.ziodyne.sometrpg.view.assets.AssetBundleLoader;
import com.ziodyne.sometrpg.view.input.BattleMapController;
import com.ziodyne.sometrpg.view.systems.SpriteRenderSystem;
import com.ziodyne.sometrpg.view.tween.TweenAccessorModule;

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

    install(new TweenAccessorModule());
    install(new FactoryModuleBuilder()
            .build(BattleMapController.Factory.class));

    install(new FactoryModuleBuilder()
            .build(SpriteRenderSystem.Factory.class));

    install(new FactoryModuleBuilder()
            .build(AssetBundleLoader.Factory.class));

  }

  @Provides
  public ObjectMapper provideJacksonMapper() {
    ObjectMapper mapper = new ObjectMapper();

    // Configure any global mapper settings here in the future.

    return mapper;
  }
}
