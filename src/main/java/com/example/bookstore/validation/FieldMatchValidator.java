package com.example.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstField;
    private String secondField;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstField = constraintAnnotation.firstField();
        secondField = constraintAnnotation.secondField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field firstField = value.getClass().getDeclaredField(this.firstField);
            Field secondField = value.getClass().getDeclaredField(this.secondField);
            firstField.setAccessible(true);
            secondField.setAccessible(true);
            Object firstValue = firstField.get(value);
            Object secondValue = secondField.get(value);
            return Objects.equals(firstValue, secondValue);
        } catch (Exception e) {
            return false;
        }
    }
}
