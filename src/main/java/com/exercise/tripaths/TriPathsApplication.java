package com.exercise.tripaths;

import com.exercise.tripaths.parser.TextTriangleParser;
import com.exercise.tripaths.shortestpath.ShortestPathFinder;
import com.exercise.tripaths.triangle.Triangle;
import com.exercise.tripaths.triangle.WeightedVertex;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@AllArgsConstructor
@SpringBootApplication
@ComponentScan("com.exercise.tripaths")
public class TriPathsApplication implements CommandLineRunner {

    @Autowired
    private final TextTriangleParser textTriangleParser;

    @Autowired
    private final ShortestPathFinder shortestPathFinder;

    public static void main(final String[] args) {
        SpringApplication.run(TriPathsApplication.class, args);
    }


    @Override
    public void run(final String... args) {

        final List<String> textTriangle = new ArrayList<>();
        final Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            textTriangle.add(sc.nextLine());
        }

        final Triangle triangle = textTriangleParser.parse(textTriangle);

        if (triangle.isEmpty()) {
            System.out.println("Triangle is empty. There is no shortest path to find.");
        } else {
            final List<Long> shortestPath = shortestPathFinder.find(triangle.getGraph(), triangle.getStartingVertex()).stream().map(WeightedVertex::getWeight).collect(Collectors.toList());
            System.out.println("Shortest path is: " + shortestPath);
        }
    }
}
