package org.jqassistant.plugin.jee.injection.test.set.javax;

import jakarta.annotation.Resource;

import javax.ejb.Stateless;

@Stateless
public class JavaxComponentWithResources {

    @Resource
    JavaxInjectableA resourceField;

    @Resource
    JavaxInjectableA resourceMethod() {
        return null;
    }
}
