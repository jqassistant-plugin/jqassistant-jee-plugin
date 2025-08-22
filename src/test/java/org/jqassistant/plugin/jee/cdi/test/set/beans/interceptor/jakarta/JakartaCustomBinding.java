package org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.jakarta;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@InterceptorBinding
@Retention(RUNTIME)
@Target({ METHOD, TYPE })
public @interface JakartaCustomBinding {
}
