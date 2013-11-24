package com.ziodyne.sometrpg.view.assets;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.ziodyne.sometrpg.logic.models.BattleMap;
import com.ziodyne.sometrpg.logic.models.Tile;

public class Conversion {
  private Conversion() { }

  public static BattleMap convert(TiledMap tiledMap) {
    return new BattleMap(0, new Tile[0][0]);
  }
}
