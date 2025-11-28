package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax;

import javax.transaction.Transactional;

@Transactional
public class JavaxCallingSubClassOfGenericClassWithTransactionalMethod extends JavaxGenericClassWithTransactionalMethod<Long> {

    public void anotherMethod() {
        method(1L);
    }

}
