package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaOverridingSubClassOfGenericTransactionalClass extends JakartaGenericTransactionalClass<Long> {

    @Override
    public void method(Long l) {
        super.method(l);
    }

}
