package com.cwa.springdatavalidation.validator;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class YearValidatorTest {

    final YearValidator validator = new YearValidator();

    @Mock
    ConstraintValidatorContext context;

    @ParameterizedTest
    @ValueSource(ints = {2010, 2024})
    void shouldReturnTrueWhenYearIsValid(int year) {
        ValidYear validYear = createAnnotation(2010);
        validator.initialize(validYear);

        assertTrue(validator.isValid(year, context));
    }

    @ParameterizedTest
    @ValueSource(ints = {2009, 2025})
    void shouldReturnFalseWhenYearIsNotValid(int year) {
        ValidYear validYear = createAnnotation(2010);
        validator.initialize(validYear);

        assertFalse(validator.isValid(year, context));
    }

    private ValidYear createAnnotation(int min) {
        return new ValidYear() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return ValidYear.class;
            }

            @Override
            public String message() {
                return "{javax.validation.constraints.ValidYear.message}";
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public int min() {
                return min;
            }
        };
    }

}