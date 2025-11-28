package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaOverridingSubClassOfGenericNonTransactionalClass extends JakartaGenericNonTransactionalClass<Long> {

    @Override
    public void method(Long l) {
        super.method(l);
    }

}
