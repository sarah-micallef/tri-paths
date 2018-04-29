package com.excercise.tripaths.shortestpath;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class WeightedVertex {

    @NotNull
    private final String id;

    @Min(0)
    private final int weight;

    @NotBlank
    private final String label;

    public static WeightedVertex build(final int weight) {
        return WeightedVertex.builder()
                .id(UUID.randomUUID().toString())
                .weight(weight)
                .label(String.valueOf(weight))
                .build();
    }

}
