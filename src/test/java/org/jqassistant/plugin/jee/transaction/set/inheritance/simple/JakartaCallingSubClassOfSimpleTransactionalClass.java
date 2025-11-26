package org.jqassistant.plugin.jee.transaction.set.inheritance.simple;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaCallingSubClassOfSimpleTransactionalClass extends JakartaSimpleTransactionalClass {

    public void anotherMethod() {
        method();
    }
}
