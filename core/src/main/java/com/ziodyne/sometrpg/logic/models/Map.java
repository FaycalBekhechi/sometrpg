package com.ziodyne.sometrpg.logic.models;

import com.ziodyne.sometrpg.logic.models.exceptions.GameLogicException;

public class Map {
  private final Tile[][] tiles;
  private final int width;
  private final int height;
  
  public Map(int width, int height) {
    super();
    this.width = width;
    this.height = height;
    
    tiles = new Tile[width][height];
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

  /** Checks tile movement preconditions and throws exceptions when any are violated. */
  private static void validateMove(Tile src, Tile dest) throws GameLogicException {
    Unit unitToMove = src.getOccupyingUnit();
    if (unitToMove == null) {
      throw new GameLogicException("Invalid move: source tile is unoccupied.");
    }

    if (dest.isOccupied()) {
      throw new GameLogicException("Invalid move: cannot move to occupied tile.");
    }

    if (!dest.isPassable()) {
      throw new GameLogicException("Invalid move: cannot move to impassable tile.");
    }
  }

  private void moveUnit(Tile src, Tile dest) {
    validateMove(src, dest);

    Unit movingUnit = src.getOccupyingUnit();

    src.setOccupyingUnit(null);
    dest.setOccupyingUnit(movingUnit);
  }

  static class TileNotFoundException extends GameLogicException {
    TileNotFoundException(int x, int y) {
      super(String.format("Tile at (%d, %d) does not exist.", x, y));
    }
  }
}
