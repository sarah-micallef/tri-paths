package com.excercise.tripaths.shortestpath;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectAssert;

/**
 * Custom assertion for {@link WeightedVertex}.
 */
public class WeightedVertexAssert  extends ObjectAssert<WeightedVertex> {

    private WeightedVertexAssert(final WeightedVertex actual) {
        super(actual);
    }

    public static WeightedVertexAssert assertThat(final WeightedVertex actual) {
        return new WeightedVertexAssert(actual);
    }

    public WeightedVertexAssert hasDefinedId() {
        Assertions.assertThat(actual.getId()).as("ID").isNotNull();
        return this;
    }

    public WeightedVertexAssert hasIdNotEqualTo(final String other) {
        Assertions.assertThat(actual.getId()).as("ID").isNotEqualTo(other);
        return this;
    }

    public WeightedVertexAssert hasWeight(final int expectedWeight) {
        Assertions.assertThat(actual.getWeight()).as("Weight").isEqualTo(expectedWeight);
        return this;
    }

    public WeightedVertexAssert hasLabel(final String expectedLabel) {
        Assertions.assertThat(actual.getLabel()).as("Label").isEqualTo(expectedLabel);
        return this;
    }
}