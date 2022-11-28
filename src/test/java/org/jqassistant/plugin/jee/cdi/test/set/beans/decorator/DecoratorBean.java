package org.jqassistant.plugin.jee.cdi.test.set.beans.decorator;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.Bean;

@Decorator
public class DecoratorBean extends Bean {

    @Inject
    @Delegate
    @Any
    private Bean delegate;

}
