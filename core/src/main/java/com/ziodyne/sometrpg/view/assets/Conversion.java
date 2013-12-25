package com.ziodyne.sometrpg.view.assets;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.Tile;

import java.util.HashSet;

public class Conversion {
  private Conversion() { }

  public static BattleMap convert(TiledMap tiledMap) {
    return new BattleMap(new HashSet<Tile>());
  }
}
