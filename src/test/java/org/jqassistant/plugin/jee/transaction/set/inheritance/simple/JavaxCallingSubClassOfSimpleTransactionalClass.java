package org.jqassistant.plugin.jee.transaction.set.inheritance.simple;

import javax.transaction.Transactional;

@Transactional
public class JavaxCallingSubClassOfSimpleTransactionalClass extends JavaxSimpleTransactionalClass {

    public void anotherMethod() {
        method();
    }
}
