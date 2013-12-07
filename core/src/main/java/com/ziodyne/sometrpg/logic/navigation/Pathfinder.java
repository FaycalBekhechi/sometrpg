package com.ziodyne.sometrpg.logic.navigation;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;


public class Pathfinder<T> {
  // TODO: Wrap these in an interface
  private final Function<T, Double> heuristicFunction;
  private final Function<T, Double> trueCostFunction;
  private Function<T, Double> movementCostFunction;
  private Function<T, Set<T>> getNeighbors;
  private Function<Pair<T, T>, Double> distanceFunction;
  private final Predicate<T> goalCheckFunction;

  private class NodeCostComparator implements Comparator<T> {
    @Override
    public int compare(T lhs, T rhs) {
      // TODO: Cache these?
      double lCost = computeCost(lhs);
      double rCost = computeCost(rhs);

      if (lCost < rCost) {
        return -1;
      } else if (lCost > rCost) {
        return 1;
      }

      return 0;
    }
  }

  public Pathfinder(Function<T, Double> heuristicFunction, Function<T, Double> trueCostFunction, Predicate<T> goalCheckFunction) {
    this.heuristicFunction = heuristicFunction;
    this.trueCostFunction = trueCostFunction;
    this.goalCheckFunction = goalCheckFunction;
  }

  public Optional<Path<T>> computePath(T start, T goal) {
    Set<T> closedNodes = Sets.newHashSet();
    Map<T, Double> exactCosts = Maps.newHashMap();
    Map<T, Double> estimatedCosts = Maps.newHashMap();
    Map<T, T> breadcrumbs = Maps.newHashMap();
    Queue<T> openNodes = new PriorityQueue<T>(1, new NodeCostComparator());
    openNodes.add(start);

    exactCosts.put(start, 0d);
    estimatedCosts.put(start, estimatedCost(start));


    while (!openNodes.isEmpty()) {
      T currentCheapest = openNodes.poll();
      if (isGoal(currentCheapest)) {
        return Optional.of(constructPath(breadcrumbs, goal));
      }

      openNodes.remove(currentCheapest);
      closedNodes.add(currentCheapest);
      Set<T> neighbors = getNeighbors.apply(currentCheapest);
      for (T neighbor : neighbors) {
        double neighborCost = exactCosts.get(currentCheapest) + distance(currentCheapest, neighbor);
        double heuristicScore = neighborCost + estimatedCost(neighbor);
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

    return Optional.absent();
  }

  private Path<T> constructPath(Map<T, T> breadcrumbs, T currentNode) {
    return constructPath(breadcrumbs, new Path.Builder<T>(), currentNode);
  }

  private Path<T> constructPath(Map<T, T> breadCrumbs, Path.Builder<T> pathBuilder, T currentNode) {
    if (breadCrumbs.containsKey(currentNode)) {
      return constructPath(breadCrumbs, pathBuilder.addPoint(currentNode), breadCrumbs.get(currentNode));
    } else {
      return pathBuilder.build();
    }
  }

  private double distance(T start, T end) {
    Double distance = distanceFunction.apply(Pair.of(start, end));
    if (distance == null) {
      throw new IllegalArgumentException("Distance function returned null.");
    }

    return distance;
  }

  private boolean isGoal(T node) {
    return goalCheckFunction.apply(node);
  }

  private double trueCost(T node) {
    Double trueCost = trueCostFunction.apply(node);
    if (trueCost == null) {
      throw new IllegalArgumentException("True cost function returned null.");
    }

    return trueCost;
  }

  private double estimatedCost(T node) {
    Double costEstimate = heuristicFunction.apply(node);
    if (costEstimate == null) {
      throw new IllegalArgumentException("Heuristic function returned null.");
    }

    return costEstimate;
  }

  private double computeCost(T node) {
    return estimatedCost(node) + trueCost(node);
  }
}
