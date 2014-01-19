package com.ziodyne.sometrpg.logic.navigation;

import com.badlogic.gdx.math.GridPoint2;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logging.GdxLogger;
import com.ziodyne.sometrpg.logging.Logger;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.util.MathUtils;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.graph.SimpleGraph;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FloydWarshallRangeFinder implements RangeFinder {
  private static Logger logger = new GdxLogger(FloydWarshallRangeFinder.class);

  private static Equivalence.Wrapper<GridPoint2> wrap(GridPoint2 input) {
    return MathUtils.GRID_POINT_EQUIV.wrap(input);
  }

  @Override
  public Set<GridPoint2> computeRange(BattleMap map, GridPoint2 start, final int maxDistance) {
    logger.log(String.format("Start from (%s, %s)", start.x, start.y));
    logger.log(String.format("7,7 passable %s", map.isPassable(7, 7)));
    Graph<Equivalence.Wrapper<GridPoint2>, DefaultEdge> graph = new SimpleGraph<Equivalence.Wrapper<GridPoint2>, DefaultEdge>(DefaultEdge.class);
    List<GridPoint2> points = new ArrayList<GridPoint2>();
    for (int i = 0; i < map.getHeight(); i++) {
      for (int j = 0; j < map.getWidth(); j++) {
        GridPoint2 point = new GridPoint2(i, j);
        if (MathUtils.manhattanDistance(start, point) < maxDistance &&
            map.isPassable(point.x, point.y)) {
          points.add(point);
          graph.addVertex(wrap(point));
        }
      }
    }

    for (GridPoint2 point : points) {
      Equivalence.Wrapper<GridPoint2> wrappedPoint = wrap(point);
      for (GridPoint2 neighbor : MathUtils.getNeighbors(point)) {
        if (map.tileExists(neighbor.x, neighbor.y) &&
            map.isPassable(neighbor.x, neighbor.y)) {

          Equivalence.Wrapper<GridPoint2> wrappedNeighbor = wrap(neighbor);

          if (graph.containsVertex(wrappedNeighbor)) {
            if (!graph.containsEdge(wrappedPoint, wrappedNeighbor)) {
              logger.log(String.format("Adding edge from (%s, %s) to (%s, %s).", point.x, point.y, neighbor.x, neighbor.y));
              graph.addEdge(wrappedPoint, wrappedNeighbor);
            }
          }
        }
      }
    }

    FloydWarshallShortestPaths<Equivalence.Wrapper<GridPoint2>, DefaultEdge> allShortestPaths =
            new FloydWarshallShortestPaths<Equivalence.Wrapper<GridPoint2>, DefaultEdge>(graph);

    List<GraphPath<Equivalence.Wrapper<GridPoint2>, DefaultEdge>> shortestPathsFromStart =
            allShortestPaths.getShortestPaths(wrap(start));

    return Sets.newHashSet(FluentIterable.from(shortestPathsFromStart)
      .filter(new Predicate<GraphPath<Equivalence.Wrapper<GridPoint2>, DefaultEdge>>() {
        @Override
        public boolean apply(@Nullable GraphPath<Equivalence.Wrapper<GridPoint2>, DefaultEdge> input) {
          if (input == null) {
            return false;
          }

          return input.getEdgeList().size() <= maxDistance;
        }
      })
      .transform(new Function<GraphPath<Equivalence.Wrapper<GridPoint2>, DefaultEdge>, GridPoint2>() {
        @Nullable
        @Override
        public GridPoint2 apply(@Nullable GraphPath<Equivalence.Wrapper<GridPoint2>, DefaultEdge> input) {
          if (input == null) {
            return null;
          }

          GridPoint2 vertex = input.getEndVertex().get();
          logger.log(String.format("End path on (%s, %s).", vertex.x, vertex.y));

          return vertex;
        }
      }));

  }
}
