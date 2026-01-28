package org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.javax;

import javax.interceptor.*;

import java.io.Serializable;

/**
 * An interceptor that is not activated by the beans.xml file.
 */
@Interceptor
public class JavaxInactiveCustomInterceptor implements Serializable {

    @AroundInvoke
    public Object aroundInvoke(InvocationContext invocationContext) throws Exception {
        return invocationContext.proceed();
    }

    @AroundTimeout
    public Object aroundTimeout(InvocationContext invocationContext) throws Exception {
        return invocationContext.proceed();
    }

    @AroundConstruct
    public Object aroundTConstruct(InvocationContext invocationContext) throws Exception {
        return invocationContext.proceed();
    }
}
