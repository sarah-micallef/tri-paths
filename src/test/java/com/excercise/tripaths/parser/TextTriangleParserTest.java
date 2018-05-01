package com.excercise.tripaths.parser;

import com.excercise.tripaths.GraphTestWrapper;
import com.excercise.tripaths.shortestpath.WeightedVertex;
import com.excercise.tripaths.shortestpath.WeightedVertexAssert;
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
    public void parse_nullOrEmpty_shouldReturnEmptyGraph(final List<String> textFormatTriangle) {
        final Graph<WeightedVertex, DefaultEdge> parsed = parser.parse(textFormatTriangle);
        Assertions.assertThat(parsed)
                .isNotNull()
                .satisfies(graph -> Assertions.assertThat(graph.vertexSet()).isEmpty());
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
                {"Invalid delimiter", Arrays.asList("1", "2,3", "4, 5", "4   5")}
        };
    }

    @Test
    public void parse_validTextFormatTriangle_shouldConstructValidGraph() {
        final Graph<WeightedVertex, DefaultEdge> parsed = parser.parse(Arrays.asList("7", "6 3", "3 8 5", "11 2 10 9"));
        Assertions.assertThat(parsed).isNotNull();
        Assertions.assertThat(parsed.vertexSet()).hasSize(10);
        Assertions.assertThat(parsed.edgeSet()).hasSize(12);

        final GraphTestWrapper graphTestWrapper = GraphTestWrapper.wrap(parsed);

        // row 1
        final Optional<WeightedVertex> r1v7 = graphTestWrapper.findAnyVertex(7);
        Assertions.assertThat(r1v7)
                .hasValueSatisfying(v -> {
                    WeightedVertexAssert.assertThat(v).hasDefinedId().hasLabel("7").hasWeight(7);
                    Assertions.assertThat(parsed.inDegreeOf(v)).isEqualTo(0);
                });

        final Map<Long, WeightedVertex> r1v7NeighboursByWeight = graphTestWrapper.getNeighbourListOfByWeight(r1v7.get());
        Assertions.assertThat(r1v7NeighboursByWeight)
                .hasSize(2)
                .hasEntrySatisfying(6L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(6).hasLabel("6"))
                .hasEntrySatisfying(3L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(3).hasLabel("3"));

        // row 2
        final Map<Long, WeightedVertex> r2v6NeighboursByWeight = graphTestWrapper.getNeighbourListOfByWeight(r1v7NeighboursByWeight.get(6));
        Assertions.assertThat(r2v6NeighboursByWeight)
                .hasSize(2)
                .hasEntrySatisfying(3L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasIdNotEqualTo(r1v7NeighboursByWeight.get(3).getId()).hasWeight(3).hasLabel("3"))
                .hasEntrySatisfying(8L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(8).hasLabel("8"));

        final Map<Long, WeightedVertex> r2v3NeighboursByWeight = graphTestWrapper.getNeighbourListOfByWeight(r1v7NeighboursByWeight.get(3));
        Assertions.assertThat(r2v3NeighboursByWeight)
                .hasSize(2)
                .hasEntrySatisfying(8L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(8).hasLabel("8"))
                .hasEntrySatisfying(5L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(5).hasLabel("5"));

        // row 3
        final Map<Long, WeightedVertex> r3v3NeighboursByWeight = graphTestWrapper.getNeighbourListOfByWeight(r2v6NeighboursByWeight.get(3));
        Assertions.assertThat(r3v3NeighboursByWeight)
                .hasSize(2)
                .hasEntrySatisfying(11L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(11).hasLabel("11"))
                .hasEntrySatisfying(2L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(2).hasLabel("2"));

        final Map<Long, WeightedVertex> r3v8NeighboursByWeight = graphTestWrapper.getNeighbourListOfByWeight(r2v6NeighboursByWeight.get(8));
        Assertions.assertThat(r3v8NeighboursByWeight)
                .hasSize(2)
                .hasEntrySatisfying(2L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(2).hasLabel("2"))
                .hasEntrySatisfying(10L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(10).hasLabel("10"));

        final Map<Long, WeightedVertex> r3v5NeighboursByWeight = graphTestWrapper.getNeighbourListOfByWeight(r2v6NeighboursByWeight.get(5));
        Assertions.assertThat(r3v8NeighboursByWeight)
                .hasSize(2)
                .hasEntrySatisfying(10L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(10).hasLabel("10"))
                .hasEntrySatisfying(9L, v -> WeightedVertexAssert.assertThat(v).hasDefinedId().hasWeight(9).hasLabel("9"));
    }

    private Object[][] provideNullOrEmptyList() {
        return new Object[][]{
                {null},
                {Collections.emptyList()}
        };
    }

    @ImportAutoConfiguration(ValidationAutoConfiguration.class)
    public static class TextFormatTriangleParserTestConfig {

        @Bean
        public TextTriangleParser textFormatTriangleParser() {
            return new TextTriangleParser();
        }

    }

}