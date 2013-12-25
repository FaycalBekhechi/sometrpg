package com.ziodyne.sometrpg.logic.navigation;

import com.badlogic.gdx.math.GridPoint2;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.TerrainType;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.Collection;
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

    Set<Equivalence.Wrapper<GridPoint2>> allPoints = Sets.newHashSet();
    for (Tile tile : tiles) {
      allPoints.add(MathUtils.GRID_POINT_EQUIV.wrap(tile.getPosition()));
    }

    BattleMap map = new BattleMap(tiles);

    Set<GridPoint2> movablePoints = rangeFinder.computeRange(map, new GridPoint2(2, 2), 7);
    for (GridPoint2 movablePoint : movablePoints) {
      Assert.assertTrue("7 squares from the center using flood fill should cover the entire map.",
                        allPoints.contains(MathUtils.GRID_POINT_EQUIV.wrap(movablePoint)));
    }

    Set<GridPoint2> smallMovementRangePoints = rangeFinder.computeRange(map, new GridPoint2(0, 0), 1);
    Collection<Equivalence.Wrapper<GridPoint2>> equifiedPoints = Collections2.transform(smallMovementRangePoints,
            new Function<GridPoint2, Equivalence.Wrapper<GridPoint2>>() {
              @Nullable
              @Override
              public Equivalence.Wrapper<GridPoint2> apply(@Nullable GridPoint2 input) {
                return MathUtils.GRID_POINT_EQUIV.wrap(input);
              }
            });

    Assert.assertEquals("1 square movement range from the upper left corner should yeild 3 movable squares", 3,
            equifiedPoints.size());

    Assert.assertTrue("1 square movement range from the upper left corner should contain (0, 0)",
            equifiedPoints.contains(MathUtils.GRID_POINT_EQUIV.wrap(new GridPoint2(0, 0))));

    Assert.assertTrue("1 square movement range from the upper left corner should contain (1, 0)",
            equifiedPoints.contains(MathUtils.GRID_POINT_EQUIV.wrap(new GridPoint2(1, 0))));

    Assert.assertTrue("1 square movement range from the upper left corner should contain (0, 1)",
            equifiedPoints.contains(MathUtils.GRID_POINT_EQUIV.wrap(new GridPoint2(0, 1))));
  }
}
