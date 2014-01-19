package com.ziodyne.sometrpg.logic.navigation;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logging.GdxLogger;
import com.ziodyne.sometrpg.logging.Logger;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.GraphPath;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FloydWarshallRangeFinder implements RangeFinder {
  private static Logger logger = new GdxLogger(FloydWarshallRangeFinder.class);

  @Override
  public Set<GridPoint2> computeRange(BattleMap map, final GridPoint2 start, final int maxDistance) {

    Collection<GraphPath<GridPoint2, DefaultEdge>> shortestPaths = map.getAllShortestPaths();

    return Sets.newHashSet(FluentIterable.from(shortestPaths)
      .filter(new Predicate<GraphPath<GridPoint2, DefaultEdge>>() {
        @Override
        public boolean apply(@Nullable GraphPath<GridPoint2, DefaultEdge> input) {
          if (input == null) {
            return false;
          }

          if (!input.getStartVertex().equals(start)) {
            return false;
          }

          List<DefaultEdge> edges = input.getEdgeList();
          return edges.size() < maxDistance;
        }
      })
      .transform(new Function<GraphPath<GridPoint2, DefaultEdge>, GridPoint2>() {
        @Nullable
        @Override
        public GridPoint2 apply(@Nullable GraphPath<GridPoint2, DefaultEdge> input) {
          if (input == null) {
            return null;
          }

          return input.getEndVertex();
        }
      }));

  }
}
