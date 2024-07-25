package org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Stereotype
@SessionScoped
@Named
@Retention(RUNTIME)
@Target(TYPE)
public @interface CustomStereotype {
}
