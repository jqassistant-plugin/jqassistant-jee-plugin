package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaCallingSubClassOfSimpleNonTransactionalClass extends JakartaSimpleNonTransactionalClass {

    public void anotherMethod() {
        method();
    }

}
