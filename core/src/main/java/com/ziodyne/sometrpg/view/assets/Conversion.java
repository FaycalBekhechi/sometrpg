package com.ziodyne.sometrpg.view.assets;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.Tile;

public class Conversion {
  private Conversion() { }

  public static BattleMap convert(TiledMap tiledMap) {
    return new BattleMap(0, new Tile[0][0]);
  }
}
