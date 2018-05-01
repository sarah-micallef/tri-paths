package com.exercise.tripaths.parser;

import com.exercise.tripaths.GraphTestWrapper;
import com.exercise.tripaths.shortestpath.WeightedVertexAssert;
import com.exercise.tripaths.triangle.Triangle;
import com.exercise.tripaths.triangle.WeightedVertex;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.assertj.core.api.Assertions;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * Tests for {@link TextTriangleParser}
 */
@RunWith(JUnitParamsRunner.class)
@SpringBootTest(classes = TextTriangleParserTest.TextFormatTriangleParserTestConfig.class)
public class TextTriangleParserTest {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private TextTriangleParser parser;

    @Test
    @Parameters(method = "provideNullOrEmptyList")
    public void parse_nullOrEmpty_shouldReturnEmptyGraphWithoutStartingVertex(final List<String> textFormatTriangle) {
        final Triangle triangle = parser.parse(textFormatTriangle);
        Assertions.assertThat(triangle)
                .isNotNull()
                .satisfies(tri -> Assertions.assertThat(tri.getGraph().vertexSet()).isEmpty())
                .satisfies(tri -> Assertions.assertThat(tri.getStartingVertex()).isNull());
    }

    private Object[][] provideNullOrEmptyList() {
        return new Object[][]{
                {null},
                {Collections.emptyList()}
        };
    }

    @Test
    @TestCaseName("{0}")
    @Parameters(method = "provideInvalidTextFormatTriangle")
    public void parse_invalidTextFormatTriangle_shouldThrowConstraintViolationException(final String testCaseName, final List<String> invalidTextFormatTriangle) {
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> parser.parse(invalidTextFormatTriangle));
    }

    private Object[][] provideInvalidTextFormatTriangle() {
        return new Object[][]{
                {"Non-numeric weights", Arrays.asList("1", "invalid 2", "3 4 5")},

                {"Invalid delimiter", Arrays.asList("1", "2,3")},
                {"Invalid delimiter", Arrays.asList("1", "4, 5")},
                {"Invalid delimiter", Arrays.asList("1", "4   5")},

                {"Invalid number of vertices", Collections.singletonList("1 2")},
                {"Invalid number of vertices", Arrays.asList("1", "1")},
                {"Invalid number of vertices", Arrays.asList("1", "1 2 3")}
        };
    }

    @Test
    public void parse_validTextFormatTriangle_shouldConstructValidTriangle() {
        final Triangle triangle = parser.parse(Arrays.asList("7", "6 3", "3 8 5", "11 2 10 9"));

        WeightedVertexAssert.assertThat(triangle.getStartingVertex()).hasDefinedId().hasWeight(7).hasLabel("7");

        final Graph<WeightedVertex, DefaultEdge> graph = triangle.getGraph();
        Assertions.assertThat(graph).isNotNull();
        Assertions.assertThat(graph.vertexSet()).hasSize(10);
        Assertions.assertThat(graph.edgeSet()).hasSize(12);

        final GraphTestWrapper graphTestWrapper = GraphTestWrapper.wrap(graph);

        // row 1
        final Optional<WeightedVertex> r1v7 = graphTestWrapper.findAnyVertex(7);
        Assertions.assertThat(r1v7)
                .hasValueSatisfying(v -> {
                    WeightedVertexAssert.assertThat(v).hasDefinedId().hasLabel("7").hasWeight(7);
                    Assertions.assertThat(graph.inDegreeOf(v)).isEqualTo(0);
                });

        final Map<Long, WeightedVertex> r1v7NeighboursByWeight = graphTestWrapper.getSuccessorsByWeight(r1v7.get());
        Assertions.assertThat(r1v7NeighboursByWeight)
                .hasSize(2)
                .hasEntrySatisfying(6L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(6).hasLabel("6"))
                .hasEntrySatisfying(3L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(3).hasLabel("3"));

        // row 2
        final Map<Long, WeightedVertex> r2v6NeighboursByWeight = graphTestWrapper.getSuccessorsByWeight(r1v7NeighboursByWeight.get(6L));
        Assertions.assertThat(r2v6NeighboursByWeight)
                .hasSize(2)
                .hasEntrySatisfying(3L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasIdNotEqualTo(r1v7NeighboursByWeight.get(3L).getId()).hasWeight(3).hasLabel("3"))
                .hasEntrySatisfying(8L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(8).hasLabel("8"));

        final Map<Long, WeightedVertex> r2v3NeighboursByWeight = graphTestWrapper.getSuccessorsByWeight(r1v7NeighboursByWeight.get(3L));
        Assertions.assertThat(r2v3NeighboursByWeight)
                .hasSize(2)
                .hasEntrySatisfying(8L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(8).hasLabel("8"))
                .hasEntrySatisfying(5L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(5).hasLabel("5"));

        // row 3
        final Map<Long, WeightedVertex> r3v3NeighboursByWeight = graphTestWrapper.getSuccessorsByWeight(r2v6NeighboursByWeight.get(3L));
        Assertions.assertThat(r3v3NeighboursByWeight)
                .hasSize(2)
                .hasEntrySatisfying(11L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(11).hasLabel("11"))
                .hasEntrySatisfying(2L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(2).hasLabel("2"));

        final Map<Long, WeightedVertex> r3v8NeighboursByWeight = graphTestWrapper.getSuccessorsByWeight(r2v6NeighboursByWeight.get(8L));
        Assertions.assertThat(r3v8NeighboursByWeight)
                .hasSize(2)
                .hasEntrySatisfying(2L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(2).hasLabel("2"))
                .hasEntrySatisfying(10L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(10).hasLabel("10"));

        final Map<Long, WeightedVertex> r3v5NeighboursByWeight = graphTestWrapper.getSuccessorsByWeight(r2v3NeighboursByWeight.get(5L));
        Assertions.assertThat(r3v5NeighboursByWeight)
                .hasSize(2)
                .hasEntrySatisfying(10L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(10).hasLabel("10"))
                .hasEntrySatisfying(9L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(9).hasLabel("9"));
    }

    @ImportAutoConfiguration(ValidationAutoConfiguration.class)
    public static class TextFormatTriangleParserTestConfig {

        @Bean
        public TextTriangleParser textFormatTriangleParser() {
            return new TextTriangleParser();
        }

    }

}