package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.semantics.jakarta;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaSimpleTransactionalClass {

    public void methodWithRequiredSemantics() {}

    public void methodWithOverriddenSemantics() {}
}
