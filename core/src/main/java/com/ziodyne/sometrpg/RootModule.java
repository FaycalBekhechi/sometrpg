package com.ziodyne.sometrpg;

import com.badlogic.gdx.Game;
import com.google.inject.AbstractModule;

public class RootModule extends AbstractModule {
  private final Game game;

  public RootModule(Game game) {
    this.game = game;
  }

  @Override
  protected void configure() {
    bind(Game.class).toInstance(game);
  }
}
