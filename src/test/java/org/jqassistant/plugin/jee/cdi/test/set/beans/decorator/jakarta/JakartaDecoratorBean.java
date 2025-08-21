package org.jqassistant.plugin.jee.cdi.test.set.beans.decorator.jakarta;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.Bean;

@Decorator
public class JakartaDecoratorBean extends Bean {

    @Inject
    @Delegate
    @Any
    private Bean delegate;

}
