package org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype.jakarta;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Stereotype;
import jakarta.inject.Named;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Stereotype
@SessionScoped
@Named
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD})
public @interface JakartaCustomStereotype {
}