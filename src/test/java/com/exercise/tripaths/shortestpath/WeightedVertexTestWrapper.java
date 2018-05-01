package com.exercise.tripaths.shortestpath;

import com.exercise.tripaths.triangle.WeightedVertex;
import lombok.Builder;

import java.util.UUID;

/**
 * {@link WeightedVertex} test wrapper.
 */
@Builder
public class WeightedVertexTestWrapper {

    private String id;
    private int weight;
    private String label;

    public static WeightedVertexTestWrapperBuilder buildValid() {
        return WeightedVertexTestWrapper.builder()
                .id(UUID.randomUUID().toString())
                .weight(1)
                .label("A");
    }

    public WeightedVertex unwrap() {
        return WeightedVertex.builder()
                .id(this.id)
                .weight(this.weight)
                .label(this.label)
                .build();
    }

}