package com.ziodyne.sometrpg.logic.navigation;

import com.badlogic.gdx.math.GridPoint2;
import com.google.common.base.Equivalence;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.TerrainType;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class FloodFillRangeFinderTest {
  private RangeFinder rangeFinder = new FloodFillRangeFinder();

  @Test
  public void testComputeRange() throws Exception {
    Set<Tile> tiles = new HashSet<Tile>();
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        tiles.add(new Tile(TerrainType.GRASS, i, j));
      }
    }

    BattleMap map = new BattleMap(tiles);
    Set<GridPoint2> movablePoints = rangeFinder.computeRange(map, new GridPoint2(2, 2), 7);
    Set<Equivalence.Wrapper<GridPoint2>> allPoints = Sets.newHashSet();

    for (Tile tile : tiles) {
      allPoints.add(MathUtils.GRID_POINT_EQUIV.wrap(tile.getPosition()));
    }

    for (GridPoint2 movablePoint : movablePoints) {
      Assert.assertTrue("7 squares from the center using flood fill should cover the entire map.",
                        allPoints.contains(MathUtils.GRID_POINT_EQUIV.wrap(movablePoint)));
    }
  }
}
