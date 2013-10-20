package com.ziodyne.sometrpg.view.assets;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.ziodyne.sometrpg.logic.models.Map;
import com.ziodyne.sometrpg.logic.models.Tile;

public class Conversion {
  private Conversion() { }

  public static Map convert(TiledMap tiledMap) {
    return new Map(0, new Tile[0][0]);
  }
}
