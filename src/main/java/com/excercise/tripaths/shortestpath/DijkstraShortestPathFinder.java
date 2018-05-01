package com.excercise.tripaths.shortestpath;

import com.excercise.tripaths.triangle.WeightedVertex;
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
 * FIXME: REFACTOR!!!!
 */
@Slf4j
@Component
public class DijkstraShortestPathFinder implements ShortestPathFinder {

    @Override
    public List<WeightedVertex> find(@NotNull final Graph<WeightedVertex, DefaultEdge> graph, @NotNull @Valid final WeightedVertex sourceVertex) {

        if (!graph.containsVertex(sourceVertex)) {
            throw new IllegalStateException(String.format("Source vertex %s is not contained in graph.", sourceVertex));
        }

        final Set<WeightedVertex> leafNodes = new HashSet<>();
        final Map<WeightedVertex, WeightedVertex> prev = new HashMap<>();

        final Map<@NotNull WeightedVertex, Long> distances = graph.vertexSet().stream().collect(Collectors.toMap(Function.identity(), v -> sourceVertex.equals(v) ? sourceVertex.getWeight() : Integer.MAX_VALUE));
        final Set<WeightedVertex> visitedVertices = new HashSet<>();
        final Set<WeightedVertex> unvistedVertices = new HashSet<>(graph.vertexSet());

        while (!unvistedVertices.isEmpty()) {
            final WeightedVertex minDistVertex = findUnvisitedVertexOfMinDistance(unvistedVertices, distances);
            unvistedVertices.remove(minDistVertex);
            if (graph.outDegreeOf(minDistVertex) == 0) {
                leafNodes.add(minDistVertex);
            }

            final List<WeightedVertex> neighbours = Graphs.neighborListOf(graph, minDistVertex);

            neighbours.forEach(neighbour -> {
                final Long currentDstToNeighbour = distances.get(neighbour);
                final long newDstToNeighbour = distances.get(minDistVertex) + neighbour.getWeight();
                if (newDstToNeighbour < currentDstToNeighbour) {
                    distances.put(neighbour, newDstToNeighbour);
                    prev.put(neighbour, minDistVertex);
                }
            });

            visitedVertices.add(minDistVertex);
        }

        @NotNull final WeightedVertex leafNodeWithMinDst = distances.entrySet().stream().filter(entry -> leafNodes.contains(entry.getKey())).min(Comparator.comparingLong(Map.Entry::getValue)).get().getKey();
        final List<WeightedVertex> shortestPath = new ArrayList<>();
        shortestPath.add(leafNodeWithMinDst);
        WeightedVertex previous = prev.get(leafNodeWithMinDst);
        while (previous != null) {
            shortestPath.add(previous);
            previous = prev.get(previous);
        }
        Collections.reverse(shortestPath);
        return shortestPath;
    }

    WeightedVertex findUnvisitedVertexOfMinDistance(final Set<WeightedVertex> unvisitedVertices, final Map<WeightedVertex, Long> distances) {
        return distances.entrySet().stream().filter(dst -> unvisitedVertices.contains(dst.getKey())).sorted(Comparator.comparingLong(Map.Entry::getValue)).findFirst().map(Map.Entry::getKey).get();
    }

}
