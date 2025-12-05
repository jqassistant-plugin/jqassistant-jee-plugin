package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaCallingSubClassOfSimpleTransactionalClass extends JakartaSimpleTransactionalClass {

    public void anotherMethod() {
        method();
    }
}
