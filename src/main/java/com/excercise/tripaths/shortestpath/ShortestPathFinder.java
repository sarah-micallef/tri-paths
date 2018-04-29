package com.excercise.tripaths.shortestpath;

import org.jgrapht.Graph;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface ShortestPathFinder {

    /**
     * @param <E> the edge type of the graph
     * @return the shortest path in the given {@code graph} starting from the given {@code sourceVertex}.
     */
    <E> List<WeightedVertex> find(@NotNull Graph<WeightedVertex, E> graph, @NotNull @Valid WeightedVertex sourceVertex);

}
