package com.ziodyne.sometrpg.logic.models;

import com.ziodyne.sometrpg.logic.models.exceptions.GameLogicException;
import com.ziodyne.sometrpg.logic.util.ModelTestUtils;
import org.junit.Test;

public class MapTest {

  //@Test(expected = GameLogicException.class)
  public void testMoveFromUnoccupiedTile() {

  }

  //@Test(expected = GameLogicException.class)
  public void testMoveToOccupiedTile() {

  }


  @Test(expected = GameLogicException.class)
  public void testMoveFromNonexistantTile() {
    Map emptyMap = ModelTestUtils.createEmptyMap();
    emptyMap.moveUnit(20, 20, 20, 20);
  }
}
