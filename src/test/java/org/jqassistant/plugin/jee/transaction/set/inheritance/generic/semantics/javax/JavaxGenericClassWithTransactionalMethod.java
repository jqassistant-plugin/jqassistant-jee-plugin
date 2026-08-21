package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.semantics.javax;

import javax.transaction.Transactional;

public class JavaxGenericClassWithTransactionalMethod<T> {

    @Transactional
    public void methodWithRequiredSemantics(T parameter) {

    }

    @Transactional
    public void methodWithOverriddenSemantics(T parameter) {

    }

}
