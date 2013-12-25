package com.ziodyne.sometrpg.logic.models.battle;

import com.google.common.collect.Range;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.exceptions.GameLogicException;

import java.util.HashMap;

public class BattleMap {
  private final Tile[][] tiles;
  private final int width;
  private final int height;
  private final Range<Integer> mapIndices;
  private final java.util.Map<Long, Tile> occupyingUnits = new HashMap<Long, Tile>();

  public BattleMap(int size, Tile[][] tiles) {
    super();
    this.width = size;
    this.height = size;

    mapIndices = Range.closedOpen(0, size);

    if (tiles.length != size) {
      throw new GameLogicException("Non-square grid: Row of invalid size.");
    }

    for (Tile[] row : tiles) {
      if (row.length != size) {
        throw new GameLogicException("Non-square grid: Column of invalid size.");
      }
    }

    this.tiles = tiles;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public boolean isPassable(int x, int y) {
    return tileExists(x, y);
  }

  public boolean tileExists(int x, int y) {
    return getTile(x, y) != null;
  }

  /** Gets the tile at (x, y). Returns null if it does not exist. */
  public Tile getTile(int x, int y) {
    if (!mapIndices.contains(x) ||
        !mapIndices.contains(y)) {
      return null;
    }

    return tiles[x][y];
  }

  /** Move the unit from the source tile to the destination tile IFF. the destination is unoccupied and passable. */
  public void moveUnit(int srcX, int srcY, int destX, int destY) {
    Tile src = getTile(srcX, srcY);
    Tile dest = getTile(destX, destY);

    if (src == null) {
      throw new TileNotFoundException(srcX, srcY);
    }

    if (dest == null) {
      throw new TileNotFoundException(destX, destY);
    }

    moveUnit(src, dest);
  }

  /** Add a unit to the map. Blows up if it already exists. */
  public void addUnit(Combatant unit, int x, int y) {
    Tile destination = getTile(x, y);
    if (destination == null) {
      throw new TileNotFoundException(x, y);
    }

    validateDestinationTile(destination);
    Long unitId = unit.getUnitId();
    Tile existingOccupancy = occupyingUnits.get(unitId);
    if (existingOccupancy != null) {
      throw new GameLogicException("Unit with id: " + unitId + " is already present on this map.");
    }

    occupyingUnits.put(unitId, destination);
    destination.setOccupyingUnit(unit);
  }

  /** Remove a unit from the map. Blows up if it does not exist. */
  public void removeCombatant(Combatant combatant) {
    Long unitId = combatant.getUnitId();
    Tile occupancy = occupyingUnits.get(unitId);
    if (occupancy == null) {
      throw  new GameLogicException("Unit with id: " + unitId + " does not exist on this map.");
    }

    occupyingUnits.remove(unitId);
    occupancy.setOccupyingUnit(null);
  }

  public boolean hasUnit(Unit unit) {
    return occupyingUnits.get(unit.getId()) != null;
  }

  public boolean hasUnit(Combatant combatant) {
    return occupyingUnits.get(combatant.getUnitId()) != null;
  }

  private void moveUnit(Tile src, Tile dest) {
    validateMove(src, dest);

    Combatant movingUnit = src.getOccupyingUnit();

    src.setOccupyingUnit(null);
    dest.setOccupyingUnit(movingUnit);

    Long unitId = movingUnit.getUnitId();
    occupyingUnits.remove(unitId);
    occupyingUnits.put(unitId, dest);
  }

  /** Checks tile movement preconditions and throws exceptions when any are violated. */
  private static void validateMove(Tile src, Tile dest) throws GameLogicException {
    Combatant unitToMove = src.getOccupyingUnit();
    if (unitToMove == null) {
      throw new GameLogicException("Invalid move: source tile is unoccupied.");
    }

    validateDestinationTile(dest);
  }

  private static void validateDestinationTile(Tile dest) throws GameLogicException {
    if (dest.isOccupied()) {
      throw new GameLogicException("Invalid move: cannot move to occupied tile.");
    }

    if (!dest.isPassable()) {
      throw new GameLogicException("Invalid move: cannot move to impassable tile.");
    }
  }

  static class TileNotFoundException extends GameLogicException {
    TileNotFoundException(int x, int y) {
      super(String.format("Tile at (%d, %d) does not exist.", x, y));
    }
  }
}
