package com.ziodyne.sometrpg.logic.models;

import com.ziodyne.sometrpg.logic.models.exceptions.GameLogicException;
import com.ziodyne.sometrpg.logic.util.ModelTestUtils;
import org.junit.Test;

public class MapTest {

  @Test(expected = GameLogicException.class)
  public void testMoveFromUnoccupiedTile() {
    Map map = new Map(5, 5);

    map.moveUnit(2, 2, 3, 3);
  }

  @Test(expected = GameLogicException.class)
  public void testMoveToOccupiedTile() {
    Map map = new Map(5, 5);
    Unit testUnit = com.ziodyne.sometrpg.view.screens.debug.ModelTestUtils.createUnit();
    Unit otherTestUnit = com.ziodyne.sometrpg.view.screens.debug.ModelTestUtils.createUnit();

    map.addUnit(testUnit, 3, 3);
    map.addUnit(otherTestUnit, 2, 2);

    map.moveUnit(2, 2, 3, 3);
  }


  @Test(expected = GameLogicException.class)
  public void testMoveFromNonexistantTile() {
    Map emptyMap = ModelTestUtils.createEmptyMap();
    emptyMap.moveUnit(20, 20, 20, 20);
  }
}
