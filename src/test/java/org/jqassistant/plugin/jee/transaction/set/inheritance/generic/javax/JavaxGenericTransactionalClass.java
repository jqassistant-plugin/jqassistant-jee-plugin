package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax;

import javax.transaction.Transactional;

@Transactional
public class JavaxGenericTransactionalClass<T> {

    public void methodWithRequiredSemantics(T parameter) {

    }

    public void methodWithOverriddenSemantics(T parameter) {

    }

}
