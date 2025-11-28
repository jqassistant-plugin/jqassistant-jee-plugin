package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta;

import jakarta.transaction.Transactional;

public class JakartaGenericClassWithTransactionalMethod<T> {

    @Transactional
    public void method(T parameter) {

    }

}
