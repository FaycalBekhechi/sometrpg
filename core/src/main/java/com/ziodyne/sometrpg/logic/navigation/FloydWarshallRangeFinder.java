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
import org.jgrapht.GraphPath;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FloydWarshallRangeFinder implements RangeFinder {
  private static Logger logger = new GdxLogger(FloydWarshallRangeFinder.class);

  private static Equivalence.Wrapper<GridPoint2> wrap(GridPoint2 input) {
    return MathUtils.GRID_POINT_EQUIV.wrap(input);
  }

  @Override
  public Set<GridPoint2> computeRange(BattleMap map, GridPoint2 start, final int maxDistance) {

    final Equivalence.Wrapper<GridPoint2> wrappedStart = MathUtils.GRID_POINT_EQUIV.wrap(start);
    Collection<GraphPath<Equivalence.Wrapper<GridPoint2>, DefaultEdge>> shortestPaths = map.getAllShortestPaths();

    return Sets.newHashSet(FluentIterable.from(shortestPaths)
      .filter(new Predicate<GraphPath<Equivalence.Wrapper<GridPoint2>, DefaultEdge>>() {
        @Override
        public boolean apply(@Nullable GraphPath<Equivalence.Wrapper<GridPoint2>, DefaultEdge> input) {
          if (input == null) {
            return false;
          }

          if (!input.getStartVertex().equals(wrappedStart)) {
            return false;
          }

          List<DefaultEdge> edges = input.getEdgeList();
          return edges.size() < maxDistance;
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
          return vertex;
        }
      }));

  }
}
