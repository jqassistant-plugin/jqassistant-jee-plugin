package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta;

import jakarta.transaction.Transactional;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.GenericNonTransactionalClass;

@Transactional
public class JakartaOverridingSubClassOfGenericNonTransactionalClass extends GenericNonTransactionalClass<Long> {

    @Override
    public void method(Long l) {
        super.method(l);
    }

}
