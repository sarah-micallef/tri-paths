package com.exercise.tripaths.shortestpath;

import com.exercise.tripaths.triangle.WeightedVertex;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A {@link ShortestPathFinder} that makes use of Dijkstra's algorithm to find the shortest path in a graph starting from
 * a given vertex.
 * <p>
 */
@Slf4j
@Component
public class DijkstraShortestPathFinder implements ShortestPathFinder {

    @Override
    public List<WeightedVertex> find(@NotNull final Graph<WeightedVertex, DefaultEdge> graph, @NotNull @Valid final WeightedVertex srcVertex) {

        if (!graph.containsVertex(srcVertex)) {
            throw new IllegalStateException(String.format("Source vertex %s is not contained in graph.", srcVertex));
        }

        final Set<WeightedVertex> leafNodes = new HashSet<>();
        final Map<WeightedVertex, WeightedVertex> shortestPathPredecessors = new HashMap<>();
        final Map<WeightedVertex, Long> distancesFromSrcVertex = calculateInitialDistancesFromSrcVertex(graph, srcVertex);

        final Set<WeightedVertex> unvisitedVertices = new HashSet<>(graph.vertexSet());
        while (!unvisitedVertices.isEmpty()) {

            final WeightedVertex minDstVertex = findMinDstVertex(unvisitedVertices, distancesFromSrcVertex);
            unvisitedVertices.remove(minDstVertex);

            if (graph.outDegreeOf(minDstVertex) == 0) {
                leafNodes.add(minDstVertex);
            }

            Graphs.successorListOf(graph, minDstVertex)
                    .forEach(neighbour -> {
                        final Long currentDstToNeighbour = distancesFromSrcVertex.get(neighbour);
                        final long newDstToNeighbour = distancesFromSrcVertex.get(minDstVertex) + neighbour.getWeight();
                        if (newDstToNeighbour < currentDstToNeighbour) {
                            distancesFromSrcVertex.put(neighbour, newDstToNeighbour);
                            shortestPathPredecessors.put(neighbour, minDstVertex);
                        }
                    });

        }

        final WeightedVertex minDstLeafNode = findMinDstVertex(leafNodes, distancesFromSrcVertex);
        return constructPathToTargetVertex(minDstLeafNode, shortestPathPredecessors);
    }

    private Map<WeightedVertex, Long> calculateInitialDistancesFromSrcVertex(final Graph<WeightedVertex, DefaultEdge> graph, final WeightedVertex srcVertex) {
        return graph.vertexSet().stream().collect(Collectors.toMap(Function.identity(), v -> srcVertex.equals(v) ? srcVertex.getWeight() : Integer.MAX_VALUE));
    }

    private WeightedVertex findMinDstVertex(final Set<WeightedVertex> vertices, final Map<WeightedVertex, Long> distances) {
        return vertices.stream().min(Comparator.comparingLong(distances::get)).orElseThrow(() -> new IllegalStateException("No min dst vertex could be found because given collection is empty."));
    }

    private List<WeightedVertex> constructPathToTargetVertex(final WeightedVertex targetVertex, final Map<WeightedVertex, WeightedVertex> predecessors) {
        final ArrayDeque<WeightedVertex> path = new ArrayDeque<>();
        WeightedVertex pathVertex = targetVertex;
        while (pathVertex != null) {
            path.addFirst(pathVertex);
            pathVertex = predecessors.get(pathVertex);
        }
        return new ArrayList<>(path);
    }

}
