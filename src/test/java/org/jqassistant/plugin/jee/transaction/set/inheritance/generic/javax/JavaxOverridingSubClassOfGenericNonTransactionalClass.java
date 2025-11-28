package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax;

import javax.transaction.Transactional;

@Transactional
public class JavaxOverridingSubClassOfGenericNonTransactionalClass extends JavaxGenericNonTransactionalClass<Long> {

    @Override
    public void method(Long l) {
        super.method(l);
    }

}
