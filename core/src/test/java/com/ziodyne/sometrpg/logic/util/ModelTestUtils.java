package com.ziodyne.sometrpg.logic.util;

import com.ziodyne.sometrpg.logic.models.BattleMap;
import com.ziodyne.sometrpg.logic.models.TerrainType;
import com.ziodyne.sometrpg.logic.models.Tile;

public class ModelTestUtils {
  private ModelTestUtils() { }

  public static BattleMap createEmptyMap() {
    BattleMap map = new BattleMap(0, new Tile[0][0]);

    return map;
  }

  public static BattleMap createMap(int size) {
    Tile[][] tiles = new Tile[size][size];
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        tiles[row][col] = new Tile(TerrainType.GRASS);
      }
    }

    return new BattleMap(size, tiles);
  }
}
