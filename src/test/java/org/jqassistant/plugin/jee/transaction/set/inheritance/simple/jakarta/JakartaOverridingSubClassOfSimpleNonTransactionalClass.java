package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta;

import jakarta.transaction.Transactional;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.SimpleNonTransactionalClass;

@Transactional
public class JakartaOverridingSubClassOfSimpleNonTransactionalClass extends SimpleNonTransactionalClass {

    @Override
    public void method() {
        super.method();
    }

}
