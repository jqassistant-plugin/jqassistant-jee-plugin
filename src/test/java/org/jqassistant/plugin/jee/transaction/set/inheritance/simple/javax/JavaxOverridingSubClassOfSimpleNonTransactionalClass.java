package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax;

import javax.transaction.Transactional;

@Transactional
public class JavaxOverridingSubClassOfSimpleNonTransactionalClass extends JavaxSimpleNonTransactionalClass {

    @Override
    public void method() {
        super.method();
    }

}
