package com.exercise.tripaths.triangle;

import lombok.Builder;
import lombok.Getter;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class Triangle {

    @Valid
    private final WeightedVertex startingVertex;

    @Getter
    @NotNull
    private final Graph<WeightedVertex, DefaultEdge> graph;

    public boolean isEmpty() {
        return graph.vertexSet().isEmpty();
    }
}
