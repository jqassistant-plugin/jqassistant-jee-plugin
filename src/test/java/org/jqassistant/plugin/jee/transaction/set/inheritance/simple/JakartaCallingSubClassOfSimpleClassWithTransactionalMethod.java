package org.jqassistant.plugin.jee.transaction.set.inheritance.simple;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaCallingSubClassOfSimpleClassWithTransactionalMethod extends JakartaSimpleClassWithTransactionalMethod {

    public void anotherMethod() {
        method();
    }
}
