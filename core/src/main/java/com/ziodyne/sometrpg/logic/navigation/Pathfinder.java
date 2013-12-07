package com.ziodyne.sometrpg.logic.navigation;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Pathfinder<T> {
  private final Function<T, Double> heuristicFunction;
  private final Function<T, Double> trueCostFunction;
  private Function<T, Set<T>> getNeighbors;
  private final Predicate<T> goalCheckFunction;

  private class NodeCostComparator implements Comparator<T> {
    @Override
    public int compare(T lhs, T rhs) {
      return computeCost(lhs).compareTo(computeCost(rhs));
    }
  }

  public Pathfinder(Function<T, Double> heuristicFunction, Function<T, Double> trueCostFunction, Predicate<T> goalCheckFunction) {
    this.heuristicFunction = heuristicFunction;
    this.trueCostFunction = trueCostFunction;
    this.goalCheckFunction = goalCheckFunction;
  }

  public Optional<Path> computePath(T start, T goal) {
    Set<T> closedNodes = Sets.newHashSet();
    Queue<T> openNodes = new PriorityQueue<T>(1, new NodeCostComparator());
    openNodes.add(start);


    while (!goalCheckFunction.apply(openNodes.peek())) {
      T currentCheapest = openNodes.poll();
      closedNodes.add(currentCheapest);

      Set<T> neighbors = getNeighbors.apply(currentCheapest);
      Double trueCost = trueCostFunction.apply(currentCheapest);
      for(T neighbor : neighbors) {

      }
    }

    return Optional.absent();
  }

  private Double computeCost(T node) {
    Double costEstimate = heuristicFunction.apply(node);
    if (costEstimate == null) {
      throw new IllegalArgumentException("Heuristic function returned null.");
    }

    Double trueCost = trueCostFunction.apply(node);
    if (trueCost == null) {
      throw new IllegalArgumentException("True cost function returned null.");
    }

    return costEstimate + trueCost;
  }
}
