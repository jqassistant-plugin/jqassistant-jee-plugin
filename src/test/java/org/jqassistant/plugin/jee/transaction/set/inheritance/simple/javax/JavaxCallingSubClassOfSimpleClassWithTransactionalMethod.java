package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax;

import javax.transaction.Transactional;

@Transactional
public class JavaxCallingSubClassOfSimpleClassWithTransactionalMethod extends JavaxSimpleClassWithTransactionalMethod {

    public void anotherMethod() {
        method();
    }
}
