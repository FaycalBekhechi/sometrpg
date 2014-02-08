package com.ziodyne.sometrpg.logic.navigation;

import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.TerrainType;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
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

    Set<GridPoint2> allPoints = Sets.newHashSet();
    for (Tile tile : tiles) {
      allPoints.add(tile.getPosition());
    }

    BattleMap map = new BattleMap(tiles);

    Set<GridPoint2> movablePoints = rangeFinder.computeRange(map, new GridPoint2(2, 2), 7);
    for (GridPoint2 movablePoint : movablePoints) {
      Assert.assertTrue("7 squares from the center using flood fill should cover the entire map.",
                        allPoints.contains(movablePoint));
    }

    Set<GridPoint2> points = rangeFinder.computeRange(map, new GridPoint2(0, 0), 1);

    Assert.assertEquals("1 square movement range from the upper left corner should yeild 3 movable squares", 3,
            points.size());

    Assert.assertTrue("1 square movement range from the upper left corner should contain (0, 0)",
            points.contains(new GridPoint2(0, 0)));

    Assert.assertTrue("1 square movement range from the upper left corner should contain (1, 0)",
            points.contains(new GridPoint2(1, 0)));

    Assert.assertTrue("1 square movement range from the upper left corner should contain (0, 1)",
            points.contains(new GridPoint2(0, 1)));
  }
}
