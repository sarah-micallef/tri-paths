package com.excercise.tripaths;

import com.excercise.tripaths.triangle.WeightedVertex;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A test wrapper for {@link Graph}.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphTestWrapper {

    private final Graph<WeightedVertex, DefaultEdge> wrapped;

    public static GraphTestWrapper wrap(final Graph<WeightedVertex, DefaultEdge> graph) {
        return new GraphTestWrapper(graph);
    }

    public Optional<WeightedVertex> findAnyVertex(final int weight) {
        return wrapped.vertexSet().stream().filter(v -> weight == v.getWeight()).findAny();
    }

    /**
     * @apiNote use this method only when the given vertex has neighbours with unique weight.
     */
    public Map<Long, WeightedVertex> getOutgoingNeighboursOfByWeight(final WeightedVertex vertex) {
        return Graphs.successorListOf(wrapped, vertex).stream().collect(Collectors.toMap(WeightedVertex::getWeight, Function.identity()));
    }

}
