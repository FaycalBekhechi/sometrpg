package com.ziodyne.sometrpg.logic.models;

import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.exceptions.GameLogicException;
import com.ziodyne.sometrpg.logic.util.ModelTestUtils;
import org.junit.Assert;
import org.junit.Test;

public class BattleMapTest {

  private static Unit newTestUnit() {
    return com.ziodyne.sometrpg.view.screens.debug.ModelTestUtils.createUnit();
  }

  @Test(expected = GameLogicException.class)
  public void testMoveFromUnoccupiedTile() {
    BattleMap map = ModelTestUtils.createMap(5);

    map.moveUnit(2, 2, 3, 3);
  }

  @Test(expected = GameLogicException.class)
  public void testMoveToOccupiedTile() {
    BattleMap map = ModelTestUtils.createMap(5);
    Combatant testUnit = new Combatant(newTestUnit());
    Combatant otherTestUnit = new Combatant(newTestUnit());

    map.addUnit(testUnit, 3, 3);
    map.addUnit(otherTestUnit, 2, 2);

    map.moveUnit(2, 2, 3, 3);
  }

  @Test(expected = GameLogicException.class)
  public void testMoveFromNonexistantTile() {
    BattleMap emptyMap = ModelTestUtils.createEmptyMap();
    emptyMap.moveUnit(20, 20, 20, 20);
  }

  @Test(expected = GameLogicException.class)
  public void testAddUnitTwice() {
    BattleMap map = ModelTestUtils.createMap(5);
    Combatant testUnit = new Combatant(newTestUnit());

    map.addUnit(testUnit, 0, 0);
    map.addUnit(testUnit, 2, 2);
  }

  @Test
  public void testUnitAddition() {
    BattleMap map = ModelTestUtils.createMap(5);
    Combatant testUnit = new Combatant(newTestUnit());

    map.addUnit(testUnit, 2, 2);
    Assert.assertTrue(map.hasUnit(testUnit));
  }

  @Test
  public void testUnitRemoval() {
    BattleMap map = ModelTestUtils.createMap(5);
    Combatant testUnit = new Combatant(newTestUnit());

    map.addUnit(testUnit, 2, 2);
    map.removeCombatant(testUnit);

    Assert.assertFalse(map.hasUnit(testUnit));
  }

  @Test(expected = GameLogicException.class)
  public void testRemoveNonexistantUnit() {
    BattleMap map = ModelTestUtils.createMap(5);
    Unit testUnit = newTestUnit();
    Combatant combatant = new Combatant(testUnit);

    map.removeCombatant(combatant);
  }

  @Test
  public void testGetNonexistantTile() {
    BattleMap map = ModelTestUtils.createMap(10);
    Tile nonExistantTile = map.getTile(10, 10);
    Tile existantTileLower = map.getTile(0, 0);

    Assert.assertNull(nonExistantTile);
    Assert.assertNotNull(existantTileLower);
  }
}
