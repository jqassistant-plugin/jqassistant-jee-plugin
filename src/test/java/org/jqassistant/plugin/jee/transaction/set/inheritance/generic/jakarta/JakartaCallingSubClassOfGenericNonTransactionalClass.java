package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta;

import jakarta.transaction.Transactional;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.GenericNonTransactionalClass;

@Transactional
public class JakartaCallingSubClassOfGenericNonTransactionalClass extends GenericNonTransactionalClass<Long> {

    public void anotherMethod() {
        method(1L);
    }

}
