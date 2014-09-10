package com.ziodyne.sometrpg.logic.models.battle;

import java.util.HashSet;
import java.util.Set;

import com.ziodyne.sometrpg.logic.util.GridPoint2;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.GraphVertexChangeEvent;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.traverse.ClosestFirstIterator;

public class BattleGraph  {

  // This graph has edges that do not respect units standing on tiles
  private final DirectedGraph<GridPoint2, DefaultEdge> adjacencyGraph =
    new DefaultDirectedGraph<>(DefaultEdge.class);

  // This graph does not have edges between tiles with units on them
  private final ListenableDirectedGraph<GridPoint2, DefaultEdge> blockingGraph =
    new ListenableDirectedGraph<>(DefaultEdge.class);

  private final ConnectivityInspector<GridPoint2, DefaultEdge> connectivity =
    new ConnectivityInspector<>(blockingGraph);

  public BattleGraph() {
    setupConnectivityListener();
  }

  private void setupConnectivityListener() {
    blockingGraph.addGraphListener(new GraphListener<GridPoint2, DefaultEdge>() {
      @Override
      public void edgeAdded(GraphEdgeChangeEvent<GridPoint2, DefaultEdge> gridPoint2DefaultEdgeGraphEdgeChangeEvent) {
        connectivity.edgeAdded(gridPoint2DefaultEdgeGraphEdgeChangeEvent);
      }

      @Override
      public void edgeRemoved(GraphEdgeChangeEvent<GridPoint2, DefaultEdge> gridPoint2DefaultEdgeGraphEdgeChangeEvent) {
        connectivity.edgeRemoved(gridPoint2DefaultEdgeGraphEdgeChangeEvent);
      }

      @Override
      public void vertexAdded(GraphVertexChangeEvent<GridPoint2> gridPoint2GraphVertexChangeEvent) {
        connectivity.vertexAdded(gridPoint2GraphVertexChangeEvent);
      }

      @Override
      public void vertexRemoved(GraphVertexChangeEvent<GridPoint2> gridPoint2GraphVertexChangeEvent) {
        connectivity.vertexRemoved(gridPoint2GraphVertexChangeEvent);
      }
    });
  }

  public void addTileVertex(GridPoint2 tilePos) {
    if (!adjacencyGraph.containsVertex(tilePos)) {
      adjacencyGraph.addVertex(tilePos);
    }

    if (!blockingGraph.containsVertex(tilePos)) {
      blockingGraph.addVertex(tilePos);
    }
  }

  public void addTileEdge(GridPoint2 source, GridPoint2 sink) {
    adjacencyGraph.addEdge(source, sink);
    blockingGraph.addEdge(source, sink);
  }

  public void addUnitEdge(GridPoint2 source, GridPoint2 sink) {
    blockingGraph.addEdge(source, sink);
  }

  public void removeUnitEdge(GridPoint2 source, GridPoint2 sink) {
    blockingGraph.removeEdge(source, sink);
  }

  public boolean pathExists(GridPoint2 start, GridPoint2 end) {
    return connectivity.pathExists(start, end);
  }

  public Set<GridPoint2> getNeighborsInRadius(GridPoint2 start, int radius) {
    Set<GridPoint2> neighbors = new HashSet<>();
    new ClosestFirstIterator<>(adjacencyGraph, start, radius).forEachRemaining(neighbors::add);

    neighbors.remove(start);

    return neighbors;
  }

  public Set<GridPoint2> getPassableNeighborsInRadius(GridPoint2 start, int radius) {
    Set<GridPoint2> neighbors = new HashSet<>();
    new ClosestFirstIterator<>(blockingGraph, start, radius).forEachRemaining(neighbors::add);

    neighbors.remove(start);

    return neighbors;
  }
}
