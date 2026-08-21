package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.semantics.javax;

import javax.transaction.Transactional;

public class JavaxSimpleClassWithTransactionalMethod {

    @Transactional
    public void methodWithRequiredSemantics() {}

    @Transactional
    public void methodWithOverriddenSemantics() {}
}
