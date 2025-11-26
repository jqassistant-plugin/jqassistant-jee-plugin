package org.jqassistant.plugin.jee.transaction.set.inheritance.simple;

import javax.transaction.Transactional;

@Transactional
public class JavaxCallingSubClassOfSimpleClassWithTransactionalMethod extends JavaxSimpleClassWithTransactionalMethod {

    public void anotherMethod() {
        method();
    }
}
