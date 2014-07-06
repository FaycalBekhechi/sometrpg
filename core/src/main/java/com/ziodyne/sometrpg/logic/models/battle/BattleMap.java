package com.ziodyne.sometrpg.logic.models.battle;

import com.google.common.collect.Maps;
import com.ziodyne.sometrpg.logging.GdxLogger;
import com.ziodyne.sometrpg.logging.Logger;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.exceptions.GameLogicException;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.logic.util.MathUtils;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.GraphVertexChangeEvent;
import org.jgrapht.graph.ListenableUndirectedGraph;
import org.jgrapht.traverse.ClosestFirstIterator;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

public class BattleMap {
  private static final Logger logger = new GdxLogger(BattleMap.class);

  private final int width;

  private final int height;

  private final java.util.Map<Long, Tile> occupyingUnits = Maps.newHashMap();

  private final Map<GridPoint2, Tile> tilesByPosition = Maps.newHashMap();

  private final ListenableUndirectedGraph<GridPoint2, DefaultEdge> graph =
          new ListenableUndirectedGraph<GridPoint2, DefaultEdge>(DefaultEdge.class);

  private ConnectivityInspector<GridPoint2, DefaultEdge> connectivityInspector;

  public BattleMap(Collection<Tile> tiles) {
    if (tiles.isEmpty()) {
      throw new IllegalArgumentException("Cannot make a map from no tiles.");
    }

    // Get the largest tile in the X direction
    OptionalInt westmostPosition = tiles.stream()
      .mapToInt(tile -> tile.getPosition().x)
      .sorted()
      .max();
    this.width = westmostPosition.getAsInt();

    // Get the largest tile in the Y direction
    OptionalInt southmostPosition = tiles.stream()
      .mapToInt(tile -> tile.getPosition().y)
      .sorted()
      .max();
    this.height = southmostPosition.getAsInt();


    populateTileIndex(tiles);

    long start = System.currentTimeMillis();
    populateGraph(tiles);
    long end = System.currentTimeMillis();

    logger.debug("Graph population took " + (end - start) + "ms.");
  }

  private void populateGraph(Collection<Tile> tiles) {
    for (Tile tile : tiles) {
      GridPoint2 pos = tile.getPosition();
      graph.addVertex(pos);

      // Only add edges out of passable tiles
      if (tile.isPassable()) {
        for (GridPoint2 neighbor : MathUtils.getNeighbors(pos)) {

          // Only add edges into passable tiles
          if (tileExists(neighbor.x, neighbor.y) &&
              isPassable(neighbor.x, neighbor.y)) {

            if (!graph.containsVertex(neighbor)) {
              graph.addVertex(neighbor);
            }

            graph.addEdge(pos, neighbor);
          }
        }
      }
    }
    connectivityInspector = new ConnectivityInspector<>(graph);

    graph.addGraphListener(new GraphListener<GridPoint2, DefaultEdge>() {
      @Override
      public void edgeAdded(GraphEdgeChangeEvent<GridPoint2, DefaultEdge> e) {
        connectivityInspector.edgeAdded(e);
      }

      @Override
      public void edgeRemoved(GraphEdgeChangeEvent<GridPoint2, DefaultEdge> e) {
        connectivityInspector.edgeRemoved(e);
      }

      @Override
      public void vertexAdded(GraphVertexChangeEvent<GridPoint2> e) {
        connectivityInspector.vertexAdded(e);
      }

      @Override
      public void vertexRemoved(GraphVertexChangeEvent<GridPoint2> e) {
        connectivityInspector.vertexRemoved(e);
      }
    });
  }

  public Set<GridPoint2> getNeighborsInRadius(GridPoint2 start, int radius) {
    Set<GridPoint2> neighbors = new HashSet<>();
    new ClosestFirstIterator<>(graph, start, radius).forEachRemaining(neighbors::add);

    neighbors.remove(start);

    return neighbors;
  }

  public boolean pathExists(GridPoint2 start, GridPoint2 end) {
    return connectivityInspector.pathExists(start, end);
  }

  private void populateTileIndex(Collection<Tile> tiles) {
    for (Tile tile : tiles) {
      GridPoint2 pos = tile.getPosition();
      if (tilesByPosition.containsKey(pos)) {
        throw new GameLogicException("Grid with two tiles at position " + pos);
      }

      tilesByPosition.put(pos, tile);
    }
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public boolean isPassable(int x, int y) {
    Tile tile = getTile(x, y);
    return tile != null && tile.isPassable();
  }

  public boolean tileExists(int x, int y) {
    return getTile(x, y) != null;
  }

  public void setPassable(int x, int y, boolean passable) {
    if (tileExists(x, y)) {
      getTile(x, y).setPassable(passable);

      GridPoint2 point = new GridPoint2(x, y);
      if (passable) {
        for (GridPoint2 neighbor : MathUtils.getNeighbors(point)) {
          graph.addEdge(neighbor, point);
        }
      } else {
        for (GridPoint2 neighbor : MathUtils.getNeighbors(point)) {
          graph.removeEdge(neighbor, point);
        }
      }
    }
  }

  /** Gets the tile at (x, y). Returns null if it does not exist. */
  public Tile getTile(int x, int y) {
    return tilesByPosition.get(new GridPoint2(x, y));
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
  public void addUnit(Combatant unit, GridPoint2 point) {
    addUnit(unit, point.x, point.y);
  }

  /** Add a unit to the map. Blows up if it already exists. */
  public void addUnit(Combatant unit, int x, int y) {
    Tile destination = getTile(x, y);
    if (destination == null) {
      throw new TileNotFoundException(x, y);
    }

    validateDestinationTile(destination);
    Long unitId = unit.getId();
    Tile existingOccupancy = occupyingUnits.get(unitId);
    if (existingOccupancy != null) {
      throw new GameLogicException("Unit with id: " + unitId + " is already present on this map.");
    }

    occupyingUnits.put(unitId, destination);
    destination.setCombatant(unit);
  }

  /** Remove a unit from the map. Blows up if it does not exist. */
  public void removeCombatant(Combatant combatant) {
    Long unitId = combatant.getId();
    Tile occupancy = occupyingUnits.get(unitId);
    if (occupancy == null) {
      throw  new GameLogicException("Unit with id: " + unitId + " does not exist on this map.");
    }

    occupyingUnits.remove(unitId);
    occupancy.setCombatant(null);
  }

  @Nullable
  public GridPoint2 getCombatantPosition(Combatant combatant) {
    long combatantId = combatant.getId();

    for (Tile tile : tilesByPosition.values()) {
      Combatant combatantOnTile = tile.getCombatant();
      if (combatantOnTile != null && combatantOnTile.getId() == combatantId) {
        return tile.getPosition();
      }
    }

    return null;
  }

  public boolean hasUnit(Combatant combatant) {
    return occupyingUnits.get(combatant.getId()) != null;
  }

  private void moveUnit(Tile src, Tile dest) {
    validateMove(src, dest);

    Combatant movingUnit = src.getCombatant();

    src.setCombatant(null);
    dest.setCombatant(movingUnit);

    Long unitId = movingUnit.getId();
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

  static class TileNotFoundException extends GameLogicException {
    TileNotFoundException(int x, int y) {
      super(String.format("Tile at (%d, %d) does not exist.", x, y));
    }
  }
}
