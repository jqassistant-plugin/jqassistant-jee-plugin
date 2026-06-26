package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.semantics.jakarta;

import jakarta.transaction.Transactional;

public class JakartaSimpleClassWithTransactionalMethod {

    @Transactional
    public void methodWithRequiredSemantics() {}

    @Transactional
    public void methodWithOverriddenSemantics() {}
}
