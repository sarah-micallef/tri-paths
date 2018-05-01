package com.exercise.tripaths.parser.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.regex.Pattern;

/**
 * {@link TextTriangle} validator.
 */
@Slf4j
public class TextTriangleValidator implements ConstraintValidator<TextTriangle, List<String>> {

    private static final Pattern VALID_TEXT_TRIANGLE_ROW_FORMAT = Pattern.compile("^(\\d)+(\\s(\\d)+)*$");
    @Override
    public boolean isValid(final List<String> value, final ConstraintValidatorContext context) {
        log.trace("Checking text triangle validity.");

        if (value == null) {
            return true;
        }

        int rowIndex = 1;
        for (final String row : value) {

            final int expectedNumberOfDigitsInRow = rowIndex;
            if (!VALID_TEXT_TRIANGLE_ROW_FORMAT.matcher(row).matches()) {
                log.error("Row {} not valid as it does not match pattern {}", row, VALID_TEXT_TRIANGLE_ROW_FORMAT.toString());
                return false;
            }

            if (StringUtils.delimitedListToStringArray(row, " ").length != expectedNumberOfDigitsInRow) {
                log.error("Row {} not valid as it does not contain the expected number of digits [{}].", row, expectedNumberOfDigitsInRow);
                return false;
            }

            rowIndex++;
        }

        log.debug("Text triangle valid.");
        return true;
    }

}
