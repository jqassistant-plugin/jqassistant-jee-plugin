package org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype.javax;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Stereotype
@SessionScoped
@Named
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD})
public @interface JavaxCustomStereotype {
}
