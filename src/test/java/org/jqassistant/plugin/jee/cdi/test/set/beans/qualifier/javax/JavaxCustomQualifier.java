package org.jqassistant.plugin.jee.cdi.test.set.beans.qualifier.javax;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface JavaxCustomQualifier {

    String bindingValue();

    @Nonbinding
    String nonBindingValue();
}
