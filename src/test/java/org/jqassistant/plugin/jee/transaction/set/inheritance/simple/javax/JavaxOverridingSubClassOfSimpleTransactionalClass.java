package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax;

import javax.transaction.Transactional;

@Transactional
public class JavaxOverridingSubClassOfSimpleTransactionalClass extends JavaxSimpleTransactionalClass {

    @Override
    public void method() {
        super.method();
    }

}
