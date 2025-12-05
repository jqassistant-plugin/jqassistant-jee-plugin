package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaGenericTransactionalClass<T> {

    public void method(T parameter) {

    }

}
