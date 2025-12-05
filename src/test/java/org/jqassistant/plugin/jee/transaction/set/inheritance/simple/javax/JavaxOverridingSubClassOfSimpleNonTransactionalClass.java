package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax;

import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.SimpleNonTransactionalClass;
import javax.transaction.Transactional;

@Transactional
public class JavaxOverridingSubClassOfSimpleNonTransactionalClass extends SimpleNonTransactionalClass {

    @Override
    public void method() {
        super.method();
    }

}
