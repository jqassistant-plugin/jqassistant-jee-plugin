package org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.jakarta;

import java.io.Serializable;

import jakarta.interceptor.AroundConstruct;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.AroundTimeout;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@JakartaCustomBinding
public class JakartaCustomInterceptor implements Serializable {

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
