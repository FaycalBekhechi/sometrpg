package com.ziodyne.sometrpg.logic.util;

import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.TerrainType;
import com.ziodyne.sometrpg.logic.models.battle.Tile;

import java.util.HashSet;
import java.util.Set;

public class ModelTestUtils {
  private ModelTestUtils() { }

  public static BattleMap createEmptyMap() {
    BattleMap map = new BattleMap(new HashSet<Tile>());

    return map;
  }

  public static BattleMap createMap(int size) {
    Set<Tile> tiles = new HashSet<Tile>();
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        tiles.add(new Tile(TerrainType.GRASS, row, col));
      }
    }

    return new BattleMap(tiles);
  }
}
