package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta;


import jakarta.transaction.Transactional;

@Transactional
public class JakartaOverridingSubClassOfSimpleClassWithTransactionalMethod extends JakartaSimpleClassWithTransactionalMethod{

    @Override
    public void method() {
        super.method();
    }

}
