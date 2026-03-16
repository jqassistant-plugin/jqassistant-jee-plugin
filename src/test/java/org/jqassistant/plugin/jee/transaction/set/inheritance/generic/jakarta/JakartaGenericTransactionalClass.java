package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaGenericTransactionalClass<T> {

    public void methodWithRequiredSemantics(T parameter) {

    }

    public void methodWithOverriddenSemantics(T parameter) {

    }

}
