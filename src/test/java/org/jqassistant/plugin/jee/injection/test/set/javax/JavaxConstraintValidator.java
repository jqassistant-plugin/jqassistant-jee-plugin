package org.jqassistant.plugin.jee.injection.test.set.javax;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JavaxConstraintValidator implements ConstraintValidator<Annotation, Void> {
    @Override
    public boolean isValid(Void unused, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
