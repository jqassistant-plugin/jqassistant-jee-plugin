package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.semantics.jakarta;

import jakarta.transaction.Transactional;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.SimpleNonTransactionalClass;

@Transactional
public class JakartaCallingSubClassOfSimpleNonTransactionalClass extends SimpleNonTransactionalClass {

    public void anotherMethod() {
        method();
    }

}
