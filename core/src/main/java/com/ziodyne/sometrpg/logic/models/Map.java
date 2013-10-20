package com.ziodyne.sometrpg.logic.models;

import com.ziodyne.sometrpg.logic.models.exceptions.GameLogicException;

import java.util.HashMap;

public class Map {
  private final Tile[][] tiles;
  private final int width;
  private final int height;
  private final java.util.Map<Long, Tile> occupiedUnits = new HashMap<Long, Tile>();

  public Map(int size, Tile[][] tiles) {
    super();
    this.width = size;
    this.height = size;

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

  /**
   * Gets the tile at (x, y). Returns null if it does not exist.
   */
  public Tile getTile(int x, int y) {
    if (x > width || y > height) {
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

  private void moveUnit(Tile src, Tile dest) {
    validateMove(src, dest);

    Unit movingUnit = src.getOccupyingUnit();

    src.setOccupyingUnit(null);
    dest.setOccupyingUnit(movingUnit);

    Long unitId = movingUnit.getId();
    occupiedUnits.remove(unitId);
    occupiedUnits.put(unitId, dest);
  }

  public void addUnit(Unit unit, int x, int y) {
    Tile destination = getTile(x, y);
    if (destination == null) {
      throw new TileNotFoundException(x, y);
    }

    validateDestinationTile(destination);
    Long unitId = unit.getId();
    Tile existingOccupancy = occupiedUnits.get(unitId);
    if (existingOccupancy != null) {
      throw new GameLogicException("Unit with id: " + unitId + " is already present on this map.");
    }

    occupiedUnits.put(unitId, destination);
    destination.setOccupyingUnit(unit);
  }

  public void removeUnit(Unit unit) {
    Long unitId = unit.getId();
    Tile occupancy = occupiedUnits.get(unitId);
    if (occupancy == null) {
      throw  new GameLogicException("Unit with id: " + unitId + " does not exist on this map.");
    }

    occupiedUnits.remove(unitId);
    occupancy.setOccupyingUnit(null);
  }

  public boolean hasUnit(Unit unit) {
    return occupiedUnits.get(unit.getId()) != null;
  }

  /** Checks tile movement preconditions and throws exceptions when any are violated. */
  private static void validateMove(Tile src, Tile dest) throws GameLogicException {
    Unit unitToMove = src.getOccupyingUnit();
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
