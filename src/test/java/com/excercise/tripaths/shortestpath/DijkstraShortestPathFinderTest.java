package com.excercise.tripaths.shortestpath;

import com.excercise.tripaths.triangle.WeightedVertex;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.assertj.core.api.Assertions;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.List;

@SpringBootTest(classes = DijkstraShortestPathFinderTest.DijkstraShortestPathFinderTestConfig.class)
@RunWith(JUnitParamsRunner.class)
public class DijkstraShortestPathFinderTest {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private DijkstraShortestPathFinder dijkstraShortestPathFinder;

    @Test
    @Parameters(method = "provideInvalidFindParameter")
    public void find_invalidParameter_shouldThrowConstraintViolationException(final Graph<WeightedVertex, DefaultEdge> graph,
                                                                              final WeightedVertex sourceVertex,
                                                                              final String invalidParameterName,
                                                                              final Class<? extends Annotation> violatedConstraint) {

        final Throwable throwable = Assertions.catchThrowable(() -> dijkstraShortestPathFinder.find(graph, sourceVertex));
        Assertions.assertThat(throwable)
                .isInstanceOfSatisfying(ConstraintViolationException.class,
                        ex -> Assertions.assertThat(ex.getConstraintViolations())
                                .anySatisfy(violation -> {
                                    Assertions.assertThat(violation.getConstraintDescriptor().getAnnotation()).as("Violated constraint").isInstanceOf(violatedConstraint);
                                    Assertions.assertThat(violation.getPropertyPath().toString()).as("Invalid param name").endsWith(invalidParameterName);
                                }));
    }

    private Object[][] provideInvalidFindParameter() {
        return new Object[][]{
                {null, WeightedVertexTestWrapper.buildValid().build().unwrap(), "graph", NotNull.class},

                {new DefaultDirectedGraph<>(DefaultEdge.class), null, "sourceVertex", NotNull.class},

                {new DefaultDirectedGraph<>(DefaultEdge.class), WeightedVertexTestWrapper.buildValid().id(null).build().unwrap(), "sourceVertex.id", NotNull.class},

                {new DefaultDirectedGraph<>(DefaultEdge.class), WeightedVertexTestWrapper.buildValid().weight(-1).build().unwrap(), "sourceVertex.weight", Min.class},

                {new DefaultDirectedGraph<>(DefaultEdge.class), WeightedVertexTestWrapper.buildValid().label(null).build().unwrap(), "sourceVertex.label", NotBlank.class},
                {new DefaultDirectedGraph<>(DefaultEdge.class), WeightedVertexTestWrapper.buildValid().label("").build().unwrap(), "sourceVertex.label", NotBlank.class},
                {new DefaultDirectedGraph<>(DefaultEdge.class), WeightedVertexTestWrapper.buildValid().label("  ").build().unwrap(), "sourceVertex.label", NotBlank.class},
        };
    }

    @Test
    public void find_sourceVertexNotFoundInGraph_shouldThrowIllegalStateException() {
        Assertions.assertThatIllegalStateException().isThrownBy(() -> dijkstraShortestPathFinder.find(new DefaultDirectedGraph<>(DefaultEdge.class), WeightedVertexTestWrapper.buildValid().build().unwrap()));
    }

    @Test
    public void find_validParameters_shouldFindShortestPathInGraphStartingFromSourceVertex() {

        final DefaultDirectedGraph<WeightedVertex, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        final WeightedVertex a7 = WeightedVertexTestWrapper.buildValid().weight(7).label("7").id("a").build().unwrap();
        graph.addVertex(a7);

        final WeightedVertex b6 = WeightedVertexTestWrapper.buildValid().weight(6).label("6").id("b").build().unwrap();
        graph.addVertex(b6);
        final WeightedVertex c3 = WeightedVertexTestWrapper.buildValid().weight(3).label("3").id("c").build().unwrap();
        graph.addVertex(c3);

        final WeightedVertex d3 = WeightedVertexTestWrapper.buildValid().weight(3).label("3").id("d").build().unwrap();
        graph.addVertex(d3);
        final WeightedVertex e8 = WeightedVertexTestWrapper.buildValid().weight(8).label("8").id("e").build().unwrap();
        graph.addVertex(e8);
        final WeightedVertex f5 = WeightedVertexTestWrapper.buildValid().weight(5).label("5").id("f").build().unwrap();
        graph.addVertex(f5);

        final WeightedVertex g11 = WeightedVertexTestWrapper.buildValid().weight(11).label("11").id("g").build().unwrap();
        graph.addVertex(g11);
        final WeightedVertex h2 = WeightedVertexTestWrapper.buildValid().weight(2).label("2").id("h").build().unwrap();
        graph.addVertex(h2);
        final WeightedVertex i10 = WeightedVertexTestWrapper.buildValid().weight(10).label("10").id("i").build().unwrap();
        graph.addVertex(i10);
        final WeightedVertex j9 = WeightedVertexTestWrapper.buildValid().weight(9).label("9").id("j").build().unwrap();
        graph.addVertex(j9);

        graph.addEdge(a7, b6);
        graph.addEdge(a7, c3);

        graph.addEdge(b6, d3);
        graph.addEdge(b6, e8);

        graph.addEdge(c3, e8);
        graph.addEdge(c3, f5);

        graph.addEdge(d3, g11);
        graph.addEdge(d3, h2);

        graph.addEdge(e8, h2);
        graph.addEdge(e8, i10);

        graph.addEdge(f5, i10);
        graph.addEdge(f5, j9);

        final List<WeightedVertex> shortestPath = dijkstraShortestPathFinder.find(graph, a7);
        Assertions.assertThat(shortestPath).isNotNull().hasSize(4);

        Assertions.assertThat(shortestPath.stream().map(WeightedVertex::getId)).containsExactly("a", "b", "d", "h");

    }

    @ImportAutoConfiguration(ValidationAutoConfiguration.class)
    public static class DijkstraShortestPathFinderTestConfig {

        @Bean
        public DijkstraShortestPathFinder dijkstraShortestPathFinder() {
            return new DijkstraShortestPathFinder();
        }

    }

}