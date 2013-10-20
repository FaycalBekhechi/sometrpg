package com.ziodyne.sometrpg.logic.models;

import com.ziodyne.sometrpg.logic.models.exceptions.GameLogicException;
import com.ziodyne.sometrpg.logic.util.ModelTestUtils;
import org.junit.Assert;
import org.junit.Test;

public class MapTest {

  private static Unit newTestUnit() {
    return com.ziodyne.sometrpg.view.screens.debug.ModelTestUtils.createUnit();
  }

  @Test(expected = GameLogicException.class)
  public void testMoveFromUnoccupiedTile() {
    Map map = ModelTestUtils.createMap(5);

    map.moveUnit(2, 2, 3, 3);
  }

  @Test(expected = GameLogicException.class)
  public void testMoveToOccupiedTile() {
    Map map = ModelTestUtils.createMap(5);
    Unit testUnit = newTestUnit();
    Unit otherTestUnit = newTestUnit();

    map.addUnit(testUnit, 3, 3);
    map.addUnit(otherTestUnit, 2, 2);

    map.moveUnit(2, 2, 3, 3);
  }


  @Test(expected = GameLogicException.class)
  public void testMoveFromNonexistantTile() {
    Map emptyMap = ModelTestUtils.createEmptyMap();
    emptyMap.moveUnit(20, 20, 20, 20);
  }

  @Test(expected = GameLogicException.class)
  public void testAddUnitTwice() {
    Map map = ModelTestUtils.createMap(5);
    Unit testUnit = newTestUnit();

    map.addUnit(testUnit, 0, 0);
    map.addUnit(testUnit, 2, 2);
  }

  @Test
  public void testUnitAddition() {
    Map map = ModelTestUtils.createMap(5);
    Unit testUnit = newTestUnit();

    map.addUnit(testUnit, 2, 2);
    Assert.assertTrue(map.hasUnit(testUnit));
  }

  @Test
  public void testUnitRemoval() {
    Map map = ModelTestUtils.createMap(5);
    Unit testUnit = newTestUnit();

    map.addUnit(testUnit, 2, 2);
    map.removeUnit(testUnit);

    Assert.assertFalse(map.hasUnit(testUnit));
  }

  @Test(expected = GameLogicException.class)
  public void testRemoveNonexistantUnit() {
    Map map = ModelTestUtils.createMap(5);
    Unit testUnit = newTestUnit();

    map.removeUnit(testUnit);
  }
}
