package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaOverridingSubClassOfGenericClassWithTransactionalMethod extends JakartaGenericClassWithTransactionalMethod<Long> {

    @Override
    public void method(Long l) {
        super.method(l);
    }

}
