package com.ziodyne.sometrpg.logic.models.battle;

import com.badlogic.gdx.math.GridPoint2;
import com.google.common.base.Equivalence;
import com.google.common.collect.Maps;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.exceptions.GameLogicException;
import com.ziodyne.sometrpg.logic.util.MathUtils;

import java.util.Collection;
import java.util.Map;

public class BattleMap {
  private final int width;
  private final int height;
  private final java.util.Map<Long, Tile> occupyingUnits = Maps.newHashMap();
  private final Map<Equivalence.Wrapper<GridPoint2>, Tile> tilesByPosition = Maps.newHashMap();

  public BattleMap(Collection<Tile> tiles) {
    validateSquareness(tiles);

    double sqrt = Math.sqrt(tiles.size());
    int size = (int)sqrt;
    this.width = this.height = size;

    populateTileIndex(tiles);
  }

  private void populateTileIndex(Collection<Tile> tiles) {
    for (Tile tile : tiles) {
      GridPoint2 pos = tile.getPosition();
      Equivalence.Wrapper<GridPoint2> posKey = getTileIndexKey(pos);
      if (tilesByPosition.containsKey(posKey)) {
        throw new GameLogicException("Grid with two tiles at position " + pos);
      }

      tilesByPosition.put(posKey, tile);
    }
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
    return tilesByPosition.get(getTileIndexKey(new GridPoint2(x, y)));
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
    destination.setCombatant(unit);
  }

  /** Remove a unit from the map. Blows up if it does not exist. */
  public void removeCombatant(Combatant combatant) {
    Long unitId = combatant.getUnitId();
    Tile occupancy = occupyingUnits.get(unitId);
    if (occupancy == null) {
      throw  new GameLogicException("Unit with id: " + unitId + " does not exist on this map.");
    }

    occupyingUnits.remove(unitId);
    occupancy.setCombatant(null);
  }

  public GridPoint2 getCombatantPosition(Combatant combatant) {
    long combatantId = combatant.getUnitId();

    for (Tile tile : tilesByPosition.values()) {
      if (tile.getCombatant().getUnitId() == combatantId) {
        return tile.getPosition();
      }
    }

    return null;
  }

  public boolean hasUnit(Unit unit) {
    return occupyingUnits.get(unit.getId()) != null;
  }

  public boolean hasUnit(Combatant combatant) {
    return occupyingUnits.get(combatant.getUnitId()) != null;
  }

  private void moveUnit(Tile src, Tile dest) {
    validateMove(src, dest);

    Combatant movingUnit = src.getCombatant();

    src.setCombatant(null);
    dest.setCombatant(movingUnit);

    Long unitId = movingUnit.getUnitId();
    occupyingUnits.remove(unitId);
    occupyingUnits.put(unitId, dest);
  }

  /** Checks tile movement preconditions and throws exceptions when any are violated. */
  private static void validateMove(Tile src, Tile dest) throws GameLogicException {
    Combatant unitToMove = src.getCombatant();
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

  private static void validateSquareness(Collection<Tile> tiles) {
    double sqrt = Math.sqrt(tiles.size());
    int size = (int)sqrt;
    if (size != sqrt) {
      throw new GameLogicException("Non-square grid!");
    }
  }

  private static Equivalence.Wrapper<GridPoint2> getTileIndexKey(GridPoint2 key) {
    return MathUtils.GRID_POINT_EQUIV.wrap(key);
  }

  static class TileNotFoundException extends GameLogicException {
    TileNotFoundException(int x, int y) {
      super(String.format("Tile at (%d, %d) does not exist.", x, y));
    }
  }
}
