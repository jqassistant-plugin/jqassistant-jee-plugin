package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax;

import javax.transaction.Transactional;

@Transactional
public class JavaxCallingSubClassOfSimpleNonTransactionalClass extends JavaxSimpleNonTransactionalClass {

    public void anotherMethod() {
        method();
    }

}
