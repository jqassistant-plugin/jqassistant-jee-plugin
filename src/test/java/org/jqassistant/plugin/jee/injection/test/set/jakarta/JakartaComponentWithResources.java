package org.jqassistant.plugin.jee.injection.test.set.jakarta;

import jakarta.annotation.Resource;

import javax.ejb.Stateless;

@Stateless
public class JakartaComponentWithResources {

    @Resource
    JakartaInjectableA resourceField;

    @Resource
    JakartaInjectableA resourceMethod() {
        return null;
    }
}
