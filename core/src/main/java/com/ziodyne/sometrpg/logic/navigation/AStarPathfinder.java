package com.ziodyne.sometrpg.logic.navigation;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;


public class AStarPathfinder<T> implements Pathfinder<T> {
  private final PathfindingStrategy<T> strategy;

  public AStarPathfinder(PathfindingStrategy<T> strategy) {
    this.strategy = strategy;
  }

  public Optional<Path<T>> computePath(T start, T goal) {
    if (!strategy.isPassable(goal)) {
      return Optional.empty();
    }

    final Map<T, Double> exactCosts = Maps.newHashMap();
    final Map<T, Double> estimatedCosts = Maps.newHashMap();
    Set<T> closedNodes = Sets.newHashSet();
    Map<T, T> breadcrumbs = Maps.newHashMap();

    Comparator<T> nodeComparator = new Comparator<T>() {
      @Override
      public int compare(T lhs, T rhs) {
        double lCost = computeCost(lhs);
        double rCost = computeCost(rhs);

        if (lCost < rCost) {
          return -1;
        } else if (lCost > rCost) {
          return 1;
        }

        return 0;
      }

      private double computeCost(T node) {
        return estimatedCosts.get(node) + exactCosts.get(node);
      }
    };

    Queue<T> openNodes = new PriorityQueue<>(1, nodeComparator);
    openNodes.add(start);

    exactCosts.put(start, 0d);
    estimatedCosts.put(start, strategy.estimateCost(start, goal));


    while (!openNodes.isEmpty()) {
      T currentCheapest = openNodes.poll();
      if (strategy.isGoal(currentCheapest, goal)) {
        return Optional.of(constructPath(start, breadcrumbs, goal));
      }

      openNodes.remove(currentCheapest);
      closedNodes.add(currentCheapest);
      Set<T> neighbors = strategy.getNeighbors(currentCheapest);
      for (T neighbor : neighbors) {
        double neighborCost = exactCosts.get(currentCheapest) + strategy.distance(currentCheapest, neighbor);
        double heuristicScore = neighborCost + strategy.estimateCost(neighbor, goal);
        Double previousEstimate = estimatedCosts.get(neighbor);

        if (closedNodes.contains(neighbor) && heuristicScore >= previousEstimate) {
          continue;
        }

        if (!openNodes.contains(neighbor) || heuristicScore > previousEstimate) {
          breadcrumbs.put(neighbor, currentCheapest);
          exactCosts.put(neighbor, neighborCost);
          estimatedCosts.put(neighbor, heuristicScore);
          openNodes.add(neighbor);
        }
      }
    }

    return Optional.empty();
  }

  private Path<T> constructPath(T start, Map<T, T> breadcrumbs, T currentNode) {
    return constructPath(breadcrumbs, new Path.Builder<>(start), currentNode);
  }

  private Path<T> constructPath(Map<T, T> breadCrumbs, Path.Builder<T> pathBuilder, T currentNode) {
    if (breadCrumbs.containsKey(currentNode)) {
      return constructPath(breadCrumbs, pathBuilder.addPoint(currentNode), breadCrumbs.get(currentNode));
    } else {
      return pathBuilder.build();
    }
  }
}
