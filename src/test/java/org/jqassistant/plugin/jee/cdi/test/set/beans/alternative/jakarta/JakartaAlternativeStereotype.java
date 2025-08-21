package org.jqassistant.plugin.jee.cdi.test.set.beans.alternative.jakarta;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Named;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Alternative
@SessionScoped
@Named
@Retention(RUNTIME)
@Target(TYPE)
public @interface JakartaAlternativeStereotype {
}
