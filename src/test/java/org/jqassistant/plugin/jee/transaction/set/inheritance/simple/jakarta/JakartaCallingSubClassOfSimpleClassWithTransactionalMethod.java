package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaCallingSubClassOfSimpleClassWithTransactionalMethod extends JakartaSimpleClassWithTransactionalMethod {

    public void anotherMethod() {
        method();
    }
}
