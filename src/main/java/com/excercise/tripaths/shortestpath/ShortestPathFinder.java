package com.excercise.tripaths.shortestpath;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface ShortestPathFinder {

    /**
     * @return the shortest path in the given {@code graph} starting from the given {@code sourceVertex}.
     */
    List<WeightedVertex> find(@NotNull Graph<WeightedVertex, DefaultEdge> graph, @NotNull @Valid WeightedVertex sourceVertex);

}
