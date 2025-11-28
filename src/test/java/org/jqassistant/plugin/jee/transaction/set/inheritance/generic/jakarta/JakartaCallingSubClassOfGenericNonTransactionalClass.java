package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaCallingSubClassOfGenericNonTransactionalClass extends JakartaGenericNonTransactionalClass<Long> {

    public void anotherMethod() {
        method(1L);
    }

}
