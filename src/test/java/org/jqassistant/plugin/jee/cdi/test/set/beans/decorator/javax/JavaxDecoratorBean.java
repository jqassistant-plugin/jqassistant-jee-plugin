package org.jqassistant.plugin.jee.cdi.test.set.beans.decorator.javax;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.javax.JavaxBean;

@Decorator
public class JavaxDecoratorBean extends JavaxBean {

    @Inject
    @Delegate
    @Any
    private JavaxBean delegate;

}
