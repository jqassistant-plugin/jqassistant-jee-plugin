package org.jqassistant.plugin.jee.injection.test.set.jakarta;

import java.lang.annotation.Annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class JakartaConstraintValidator implements ConstraintValidator<Annotation, Void> {
    @Override
    public boolean isValid(Void unused, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
