package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax;

import javax.transaction.Transactional;

@Transactional
public class JavaxOverridingSubClassOfGenericClassWithTransactionalMethod extends JavaxGenericClassWithTransactionalMethod<Long> {

    @Override
    public void method(Long l) {
        super.method(l);
    }

}
