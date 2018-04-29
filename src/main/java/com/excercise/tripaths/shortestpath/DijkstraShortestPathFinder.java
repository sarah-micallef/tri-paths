package com.excercise.tripaths.shortestpath;

import org.jgrapht.Graph;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A {@link ShortestPathFinder} that makes use of Dijkstra's algorithm to find the shortest path in a graph starting from
 * a given vertex.
 */
@Component
public class DijkstraShortestPathFinder implements ShortestPathFinder {

    @Override
    public <E> List<WeightedVertex> find(@NotNull final Graph<WeightedVertex, E> graph, @NotNull @Valid final WeightedVertex sourceVertex) {
        return null;
    }

}
