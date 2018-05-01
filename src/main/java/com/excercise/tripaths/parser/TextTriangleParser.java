package com.excercise.tripaths.parser;

import com.excercise.tripaths.parser.validation.TextTriangle;
import com.excercise.tripaths.triangle.Triangle;
import com.excercise.tripaths.triangle.WeightedVertex;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@Validated
public class TextTriangleParser {

    public Triangle parse(@TextTriangle final List<String> textTriangle) {

        log.trace("Parsing text triangle.");

        final DefaultDirectedGraph<WeightedVertex, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        if (textTriangle == null || textTriangle.isEmpty()) {
            log.info("Text triangle is null or empty. Parsed graph is empty.");
            return Triangle.builder().graph(graph).build();
        }

        final List<List<WeightedVertex>> rows =
                textTriangle
                        .stream()
                        .map(row ->
                                Arrays.stream(StringUtils.delimitedListToStringArray(row, " "))
                                        .map(s -> {
                                            final WeightedVertex vertex = WeightedVertex.build(Long.valueOf(s));
                                            graph.addVertex(vertex);
                                            log.debug("Vertex {} added to graph.", vertex);
                                            return vertex;
                                        }).collect(Collectors.toList()))
                        .collect(Collectors.toList());

        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            final List<WeightedVertex> row = rows.get(rowIndex);
            log.debug("Processing row at index {}: {}", rowIndex, row);

            for (int vertexIndex = 0; vertexIndex < row.size(); vertexIndex++) {
                final WeightedVertex vertex = row.get(vertexIndex);
                log.debug("Processing vertex at index {} of row {}: {}", vertexIndex, rowIndex, vertex);

                if (rows.size() > rowIndex + 1) {
                    final List<WeightedVertex> nextRow = rows.get(rowIndex + 1);
                    graph.addEdge(vertex, nextRow.get(vertexIndex));
                    graph.addEdge(vertex, nextRow.get(vertexIndex + 1));
                }
            }

        }

        return Triangle.builder()
                .startingVertex(rows.get(0).get(0))
                .graph(graph)
                .build();
    }

}
