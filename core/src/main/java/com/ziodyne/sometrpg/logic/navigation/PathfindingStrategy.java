package com.ziodyne.sometrpg.logic.navigation;

import java.util.Set;

public interface PathfindingStrategy<T> {
  /**
   * Estimate the cost for a node
   * @param node The node to estimate
   * @return Estimate of the cost from this node to the goal
   */
  public double estimateCost(T node);

  /**
   * Get the cost for a node
   * @param node Node for which to measure the cost
   * @return The true cost from the node to the goal.
   */
  public double trueCost(T node);

  /**
   * Get the distance between two nodes
   * @param start The start position
   * @param end The end position
   * @return The distance between the start and end
   */
  public double distance(T start, T end);

  /**
   * Get a set of neighbors of a node
   * @param node The node for which to get neighbors
   * @return All the neighbors of the given node
   */
  public Set<T> getNeighbors(T node);

  /**
   * Check if a node is the goal
   * @param node The node to check
   * @return {@code true} if the node is the goal {@code false} otherwise
   */
  public boolean isGoal(T node);
}
