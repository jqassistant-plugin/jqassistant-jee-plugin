package org.jqassistant.plugin.jee.transaction.set.inheritance.simple;

import jakarta.transaction.Transactional;

public class JakartaSimpleClassWithTransactionalMethod {

    @Transactional
    public void method() {}
}
