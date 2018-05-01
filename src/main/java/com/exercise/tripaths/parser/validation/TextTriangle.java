package com.exercise.tripaths.parser.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>A validation constraint for a text triangle. A text triangle should be a list of strings of space-delimited long values.
 * The first row of a text triangle should contain only one digit. Subsequent rows should contain one more digit than the previous row.</p>
 *
 * <p>
 *     An example of a valid text triangle is:
 *     <pre>
 *         1
 *         2 3
 *         4 5 6
 *         7 8 9 10
 *     </pre>
 * </p>
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TextTriangleValidator.class)
public @interface TextTriangle {

    String message() default "invalid text format triangle";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
