package com.ziodyne.sometrpg.view.assets.models;

import com.badlogic.gdx.maps.tiled.TiledMap;

public class Chapter {
  private final CharacterSprites sprites;
  private final TiledMap map;

  public Chapter(CharacterSprites sprites, TiledMap map) {

    this.sprites = sprites;
    this.map = map;
  }

  public CharacterSprites getSprites() {

    return sprites;
  }

  public TiledMap getMap() {

    return map;
  }
}
