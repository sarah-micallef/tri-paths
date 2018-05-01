package com.excercise.tripaths.parser.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A validation constraint for a text triangle. A text triangle should be a list of strings of space-delimited long values.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TextTriangleValidator.class)
public @interface TextTriangle {

    String message() default "invalid text format triangle";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
