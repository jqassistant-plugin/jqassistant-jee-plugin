package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax;

import javax.transaction.Transactional;

@Transactional
public class JavaxCallingSubClassOfGenericNonTransactionalClass extends JavaxGenericNonTransactionalClass<Long> {

    public void anotherMethod() {
        method(1L);
    }

}
