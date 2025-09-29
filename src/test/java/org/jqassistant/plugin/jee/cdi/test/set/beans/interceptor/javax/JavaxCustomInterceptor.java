package org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.javax;

import java.io.Serializable;

import javax.interceptor.AroundConstruct;
import javax.interceptor.AroundInvoke;
import javax.interceptor.AroundTimeout;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@JavaxCustomBinding
public class JavaxCustomInterceptor implements Serializable {

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
