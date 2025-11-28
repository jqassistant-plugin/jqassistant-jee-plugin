package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaOverridingSubClassOfSimpleNonTransactionalClass extends JakartaSimpleNonTransactionalClass {

    @Override
    public void method() {
        super.method();
    }

}
