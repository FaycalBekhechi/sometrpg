package com.ziodyne.sometrpg.logic.navigation;

import java.util.Set;

public interface PathfindingStrategy<T> {
  /**
   * Estimate the cost for a node
   * @param node The node to estimate
   * @param goal The requested goal node
   * @return Estimate of the cost from this node to the goal
   */
  public double estimateCost(T node, T goal);

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
   * @param goal The requested goal node
   * @return {@code true} if the node is the goal {@code false} otherwise
   */
  public boolean isGoal(T node, T goal);
}
