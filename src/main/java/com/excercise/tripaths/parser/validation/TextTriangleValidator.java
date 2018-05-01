package com.excercise.tripaths.parser.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.regex.Pattern;

/**
 * {@link TextTriangle} validator.
 */
public class TextTriangleValidator implements ConstraintValidator<TextTriangle, List<String>> {

    private static final Pattern VALID_TEXT_TRIANGLE_ROW_FORMAT = Pattern.compile("^(\\d)+(\\s.(\\d)+)*$");
    @Override
    public boolean isValid(final List<String> value, final ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        boolean matches = true;
        for (final String row : value) {
            matches = matches && VALID_TEXT_TRIANGLE_ROW_FORMAT.matcher(row).matches();
        }
        return matches;
    }

}
