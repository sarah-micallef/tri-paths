package com.excercise.tripaths.parser;

import com.excercise.tripaths.shortestpath.WeightedVertex;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Component
@Validated
public class TextTriangleParser {

    public Graph<WeightedVertex, DefaultEdge> parse(@Valid final List<String> textTriangle) {
        return new DefaultDirectedGraph<>(DefaultEdge.class);
    }

}
