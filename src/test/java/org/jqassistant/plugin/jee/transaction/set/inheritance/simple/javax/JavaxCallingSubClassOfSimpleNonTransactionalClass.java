package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax;

import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.SimpleNonTransactionalClass;
import javax.transaction.Transactional;

@Transactional
public class JavaxCallingSubClassOfSimpleNonTransactionalClass extends SimpleNonTransactionalClass {

    public void anotherMethod() {
        method();
    }

}
